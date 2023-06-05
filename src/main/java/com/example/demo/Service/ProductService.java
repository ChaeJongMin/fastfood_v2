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
//    public ProductRequsetDto getProductList(String productName){
//        Product product=productRepository.findByProductName(productName).get(0);
//        ProductRequsetDto productRequsetDto=new ProductRequsetDto(product.getProductName(),product.getPrice(),product.isAllSale());
//        return productRequsetDto;
//    }
    /**************************************************************************************************************/
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByCate(int id){
        return productRepository.findByCategories_CategoryId(id).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByDessertAndSide(){
        return productRepository.findByCategories_CategoryIdOrCategories_CategoryId(4,5).stream()
                .map(ProductResponseDto::new)
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

    //제품추가페이지
    @Transactional
    public int save(ProductSaveRequestDto requestDto, MultipartFile file) throws IOException {
        int result=0;
        Categories categories=categoriesRepository.findByCategoryName(requestDto.getCateName()).get(0);
        Product product=productRepository.save(requestDto.toProductEntity(categories));

        productImageRepository.save(requestDto.toProductImageEntity(product));
        saveImgFile(file,requestDto.getName());

        optionsSave(product);
        return product.getPid();
    }
    public void saveImgFile(MultipartFile file, String name) throws IOException {
        String finalPath=BASE_PATH+name+".jpg";
        File newFile=new File(finalPath);
        file.transferTo(newFile);
    }
    @Transactional
    public void optionsSave(Product product){
        Iterable<Size> sizeList = sizeRepository.findAll();
        Iterable<Temperature> tempList = temperRepository.findAll();

        for (Size s : sizeList) {
            for (Temperature t : tempList) {
                // Set price
                String sizeName=s.getSizename();
                String tempName=t.getTempname();
                String cateName=product.getCategories().getCategoryName();

                int price = product.getPrice();
                if (sizeName.equals("Medium") || sizeName.equals("Grande")) {
                    price += 500;
                } else if (sizeName.equals("Large") || sizeName.equals("Venti")) {
                    price += 1000;
                }

                if ((cateName.equals("Coffee") && !tempName.equals("None") && (sizeName.equals("Tall") || sizeName.equals("Grande") || sizeName.equals("Venti"))) ||
                        ((cateName.equals("버거") || cateName.equals("Soda") || cateName.equals("Side") || cateName.equals("Dessert")) && tempName.equals("None") && (sizeName.equals("Small") || sizeName.equals("Medium") || sizeName.equals("Large"))) ||
                        (cateName.equals("Set") && tempName.equals("None") && (sizeName.equals("Small") || sizeName.equals("Large")))) {
                    optionInfoRepo.save(Product_option_info.builder()
                                    .size(s)
                                    .product(product)
                                    .temperature(t)
                                    .price(price)
                                    .isUse(true)
                                    .build());
                }

                // Set size by category
//				if (cateName.equals( "Coffee") && !t.getTempname().equals("None")) {
//					if(sizeName.equals("Tall") || sizeName.equals("Grande") || sizeName.equals("Venti")){
//						optionInfoRepo.save();
//					}
//				} else if ((cateName.equals("Burger") || cateName.equals("Soda")|| cateName.equals("Side") || cateName.equals("Dessert")) &&
//						tempName.equals("None")) {
//					if(sizeName.equals("Small") || sizeName.equals("Medium") || sizeName.equals("Large")){
//						optionInfoRepo.save();
//					}
//
//				} else if((cateName.equals("Set") && tempName.equals("None")) {
//					if(sizeName.equals("Small") || sizeName.equals("Large")){
//						optionInfoRepo.save();
//					}
//				}

            }
        }

    }
    @Transactional
    public int update(int id, ProductUpdateRequestDto requestDto,MultipartFile file) throws IOException {
        Product product=productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 제품이 없습니다. id=" + id));
        int priorPrice=product.getPrice();
        product.update(requestDto.getPrice(), requestDto.isAllSale());
        if(file!=null && !file.isEmpty())
            saveImgFile(file, product.getProductName());

        List<Product_option_info> optionInfoList=optionInfoRepo.findByProduct(product);
        for(Product_option_info optionInfo : optionInfoList){
            log.info("바뀐 가격: "+requestDto.getPrice() +" 원래 가격: "+product.getPrice());
            optionInfo.update(requestDto.getPrice()-priorPrice);
        }
        return id;
    }
    @Transactional
    public void delete(int id){
        //옵션 삭제
        Product product=productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 제품이 없습니다. id=" + id));

        List<Product_option_info> optionInfoList=optionInfoRepo.findByProduct(product);
        for(Product_option_info optionInfo : optionInfoList){
            optionInfoRepo.delete(optionInfo);

        }
        //이미지 삭제
        productImageRepository.deleteByProduct(product);

        //제품 삭제
        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByCateName(String cname){
        return productRepository.findByCategoryName(cname).stream().map(ProductResponseDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponseDto findByProductName(String productName){
        return new ProductResponseDto(productRepository.findByProductName(productName).get(0));
    }

    @Transactional(readOnly = true)
    public int findIdByProductName(String productName){
        return productRepository.findByProductName(productName).get(0).getPid();
    }

}
