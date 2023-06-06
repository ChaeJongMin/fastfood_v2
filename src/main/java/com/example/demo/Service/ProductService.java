package com.example.demo.Service;

import com.example.demo.domain.*;
import com.example.demo.dto.Request.ProductSaveRequestDto;
import com.example.demo.dto.Request.ProductUpdateRequestDto;
import com.example.demo.dto.Response.ProductResponseDto;
import com.example.demo.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    public static final String BASE_PATH = "D:\\Project\\fastfood_v2\\src\\main\\resources\\static\\img\\";

    private final ProductRepository productRepository;
    private final CategoriesRepository categoriesRepository;
    private final ProductImageRepository productImageRepository;
    private final SizeRepository sizeRepository;
    private final TemperRepository temperRepository;
    private final OptionInfoRepo optionInfoRepo;
    private final S3UploadFileService s3UploadFileService;
//    public ProductRequsetDto getProductList(String productName){
//        Product product=productRepository.findByProductName(productName).get(0);
//        ProductRequsetDto productRequsetDto=new ProductRequsetDto(product.getProductName(),product.getPrice(),product.isAllSale());
//        return productRequsetDto;
//    }
    /**************************************************************************************************************/
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByCate(int id) {
        return productRepository.findByCategories_CategoryId(id).stream()
                .map(product -> new ProductResponseDto(product,
                        s3UploadFileService.getFileUrl(product.getProductName())))
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByDessertAndSide(){
        return productRepository.findByCategories_CategoryIdOrCategories_CategoryId(4,5).stream()
                .map(product -> new ProductResponseDto(product,
                        "none"))
                .collect(Collectors.toList());

    }

    public List<Integer> findDrinkPrice(List<ProductResponseDto> pList){
        List<Integer> drinkPriceList=new ArrayList<>();
        for(ProductResponseDto p : pList){
            if (p.getPrice() <= 1500) {
                drinkPriceList.add(0);
            } else {

                drinkPriceList.add(p.getPrice() - 1200);
            }
        }
        return drinkPriceList;
    }

    public List<Integer> findSidePrice(List<ProductResponseDto> pList){
        List<Integer> sidePriceLists=new ArrayList<>();
        for(ProductResponseDto p : pList){
            if (p.getPrice() <= 1500) {
                sidePriceLists.add(0);
            } else {

                sidePriceLists.add(p.getPrice() - 1200);
            }
        }
        return sidePriceLists;
    }

    //제품 추가
    /*
     1. 먼저 product 테이블에 저장
     2. productImage 테이블에 저장
     3. product_option_info 테이블에 저장
     정리하면 3가지 과정이 진행되야하며 항상 먼저 product 테이블에 먼저 저장되어야한다.
     why??) productImage, product_option_info은 product 기본키를 외래키로 사용하기 떄문
     */
    @Transactional
    public int save(ProductSaveRequestDto requestDto, MultipartFile file) throws IOException {
        int result=0;
        //dto에 카테고리명을 얻어 Categories 객체를 얻어온다.
        Categories categories=categoriesRepository.findByCategoryName(requestDto.getCateName()).get(0);
        //Categories 객체를 product 객체 생성 메소드에 전달한 후 저장
        Product product=productRepository.save(requestDto.toProductEntity(categories));
        //삽입한 이미지를 저장한다.
        ProductImage productImage=requestDto.toProductImageEntity(product);
        System.out.println(productImage.getProduct()+" "+productImage.getImageName()+" ");
        productImageRepository.save(productImage);
        //삽입한 이미지를 따로 지정한 로컬 저장소에 저장한다.
        saveImgFile(file,requestDto.getName());
        //추가할 제품의 옵션을 저장한다.
        optionsSave(product);
        return product.getPid();
    }
    //이미지 파일을 로컬 저장소에 저장하는 메소드
    public void saveImgFile(MultipartFile file, String name) throws IOException {
        //저장할 경로를 지정한다. (BASE_PATH는 기본 경로)
        String finalPath=BASE_PATH+name+".jpg";
        //File 객체를 생성해 지정된 경로에 이미지 파일을 저장한다.
        File newFile=new File(finalPath);
        file.transferTo(newFile);
        s3UploadFileService.fileUpload(newFile, name+".jpg");

    }
    //옵션을 저장하는 메소드
    @Transactional
    public void optionsSave(Product product){
        //크기, 온도에 관한 모든 객체를 가져온다.
        Iterable<Size> sizeList = sizeRepository.findAll();
        Iterable<Temperature> tempList = temperRepository.findAll();

        //사이즈 크키 * 온도 크기 만큼 반복
        for (Size s : sizeList) {
            for (Temperature t : tempList) {
                // 옵션 테이블에 저장할 데이터를 지정
                String sizeName=s.getSizename();
                String tempName=t.getTempname();
                String cateName=product.getCategories().getCategoryName();

                //가격을 지정하는 로직
                //옵션에 따라 할당되는 가격은 항상 기본 제품의 가격에 기반된다.
                int price = product.getPrice();
                //사이즈가 미디엄 , 그란데(커피 전용) 일 경우 기본 제품 가격 +500
                if (sizeName.equals("Medium") || sizeName.equals("Grande")) {
                    price += 500;
                // 사이즈가 라지, 벤티(커피 전용) 일 경우 기본 제품 가격 +1000
                } else if (sizeName.equals("Large") || sizeName.equals("Venti")) {
                    price += 1000;
                }

                //1. 제품의 카테고리가 커피이며 온도가 None이 아닐 경우 (커피의 온도는 무조건 Hot or Ice 이다) 그리고 커피 전용 크기일 경우
                //2. 제품의 카테고리가 버거/탄산/사이드/디저트 이고 온도가 None (해당 제품들은 온도가 이미 지정되어 있어 None로 결정) 이고 사이즈가 음식 전용 크기일 경우
                //3. 제품의 카테고리가 세트 이고 온도가 None(2번 조건과 동일) 사이즈가 small, Large 일 경우
                if ((cateName.equals("커피") && !tempName.equals("None") && (sizeName.equals("Tall") || sizeName.equals("Grande") || sizeName.equals("Venti"))) ||
                        ((cateName.equals("버거") || cateName.equals("탄산") || cateName.equals("사이드") || cateName.equals("디저트")) && tempName.equals("None") && (sizeName.equals("Small") || sizeName.equals("Medium") || sizeName.equals("Large"))) ||
                        (cateName.equals("Set") && tempName.equals("None") && (sizeName.equals("Small") || sizeName.equals("Large")))) {
                    //결정한 데이터들로 테이블에 저장
                    optionInfoRepo.save(Product_option_info.builder()
                                    .size(s)
                                    .product(product)
                                    .temperature(t)
                                    .price(price)
                                    .isUse(true)
                                    .build());
                }

            }
        }

    }
    //수정될 떄 제품 먼저 수정되고 제품 이미지, 옵션이 수정되어야합니다.
    @Transactional
    public int update(int id, ProductUpdateRequestDto requestDto,MultipartFile file) throws IOException {
        //제품 아이디를 수정할 제품 엔티티를 가져옵니다.
        Product product=productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 제품이 없습니다. id=" + id));
        //옵션에 따른 가격을 수정하기 위해 기존 가격을 저장합니다.
        int priorPrice=product.getPrice();
        //가져온 정보를 제품 정보 수정
        log.info("품절여부: "+requestDto.isAllSale());
        product.update(requestDto.getPrice(), requestDto.isAllSale());

        //제품 이미지가 변경될 시 이미지 파일을 변경한다.
        if(file!=null && !file.isEmpty()) {
            saveImgFile(file, product.getProductName());
        }


        //해당 제품의 옵션 객체를 가져온다.
        List<Product_option_info> optionInfoList=optionInfoRepo.findByProduct(product);
        //해당 제품의 옵션들의 가격을 변경한다. (한 제품의 옵션은 여러 개이다 => 크기가 여러 개가 존재)
        for(Product_option_info optionInfo : optionInfoList){
            log.info("바뀐 가격: "+requestDto.getPrice() +" 원래 가격: "+product.getPrice());
            //변경된 금액은 변경된 가격 - 기존 가격
            optionInfo.update(requestDto.getPrice()-priorPrice);
        }
        return id;
    }
    //productImage, product_option_info 테이블은 product 기본키를 외래키를 사용
    //그래서 productImage, product_option_info 삭제 후 product를 삭제해야 한다.
    @Transactional
    public void delete(int id){
        //제품 아이디로 삭제할 제품을 얻습니다.
        Product product=productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 제품이 없습니다. id=" + id));
        //해당 제품의 옵션을 얻어옵니다.
        log.info("삭제할 제품 "+product.getProductName());
        List<Product_option_info> optionInfoList=optionInfoRepo.findByProduct(product);
        //이미지 삭제
        productImageRepository.deleteByProduct(product);
        for(Product_option_info optionInfo : optionInfoList){
            //옵션 엔티티를 삭제합니다.
            log.info("삭제할 옵션 아이디 "+optionInfo.getInfoid());
            optionInfoRepo.delete(optionInfo);
        }
        //제품 삭제
        log.info(product.getProductName()+" "+product.getPrice());
//        productRepository.deleteProduct(product.getPid());
        productRepository.delete(product);
        s3UploadFileService.delete(product.getProductName());
    }


    //카테고리명에 해당하는 제품들은 얻는 메소드
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByCateName(String cname){
        //람다식으로 Product를 ProductResponseDto로 변환 및 리스트로 만들어 반환합니다.
        return productRepository.findByCategoryName(cname).stream()
                .map(product -> new ProductResponseDto(product,
                        "none"))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponseDto findByProductName(String productName){
        return new ProductResponseDto(productRepository.findByProductName(productName).get(0),
                s3UploadFileService.getFileUrl(productName));
    }

    @Transactional(readOnly = true)
    public int findIdByProductName(String productName){
        return productRepository.findByProductName(productName).get(0).getPid();
    }

}
