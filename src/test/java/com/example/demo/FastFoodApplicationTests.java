package com.example.demo;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.swing.filechooser.FileSystemView;

import com.example.demo.domain.*;
import com.example.demo.persistence.*;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class FastFoodApplicationTests {

	
	@Autowired	
	private WorkerRepository WorkerRepo;
	@Autowired
	private CustomerRepository CustomerRepo;
	@Autowired
	private ProductRepository ProductRepo;
	@Autowired
	private CategoriesRepository CategoRepo;
	@Autowired
	private BasketRepository basketRepo;
	@Autowired
	private ProductImageRepository ProImgRepo;
	@Autowired
	private ProductImageRepository ImageRepository;
	@Autowired
	private OptionInfoRepo OptionRepository;
	@Autowired
	private SizeRepository SizeRepository;
	@Autowired
	private TemperRepository TemperRepo;
	@Autowired
	private BoardsRepository boardsRepository;

	@AfterEach
	public void cleanUp(){
		boardsRepository.deleteAll();
	}
	@Test
	public void SaveAndSelect(){
		Customer customer=CustomerRepo.findByUserId("20173349").get(0);
		String title="테스트 제목";
		String content="테스트 내용입니다.";
		String writer=customer.getUserId();

		boardsRepository.save(Boards.builder()
				.title(title)
				.content(content)
				.writer(writer)
				.customer(customer)
				.build()
		);

		//날짜 비교를 위해 java.sql.Date 객체 생성
		Date date=new Date();
		long currentTime=date.getTime();
		java.sql.Date dateToYMD=new java.sql.Date(currentTime);

		List<Boards> boardsList=boardsRepository.findAll();

		Boards boards=boardsList.get(0);
		assertThat(boards.getWriter()).isEqualTo(writer);
		assertThat(boards.getCnt()).isEqualTo(0);
		assertThat(boards.getTitle()).isEqualTo(title);
		assertThat(boards.getContent()).isEqualTo(content);
	}

//	@Test
//	void sizeadd() {
//		
//		
//	    Size none = new Size();
//	    none.setSizename("None");
//	    SizeRepository.save(none);
//		// 사이즈
//	     Size s = new Size();
//	     s.setSizename("Small");
//	     SizeRepository.save(s);
//	     
//	     Size m = new Size();
//	     m.setSizename("Medium");
//	     SizeRepository.save(m);
//	     
//	     Size l = new Size();
//	     l.setSizename("Large");
//	     SizeRepository.save(l);
//	     
//	     Size tall = new Size();
//	     tall.setSizename("Tall");
//	     SizeRepository.save(tall);
//	     
//	     Size grande = new Size();
//	     grande.setSizename("Grande");
//	     SizeRepository.save(grande);
//	     
//	     Size venti = new Size();
//	     venti.setSizename("Venti");
//	     SizeRepository.save(venti);
//	     
//	     
//	} 
	
//	@Test
//	void tempadd() {
//		
//	
//		// 온도
//		 Temperature none = new Temperature();
//		 none.setTempname("None");
//	     TemperRepo.save(none);
//	     
//		 Temperature hot = new Temperature();
//		 hot.setTempname("Hot");
//	     TemperRepo.save(hot);
//	     
//	     Temperature ice = new Temperature();
//	     ice.setTempname("Ice");
//	     TemperRepo.save(ice);
//	   
//      
//	}
	
//	@Test
//	void optionAdd() {
//		
//
//		
//		Iterable<Product> productList = ProductRepo.findAll();
//		Iterable<Size> sizeList = SizeRepository.findAll();
//		Iterable<Temperature> tempList = TemperRepo.findAll();
//		for(Product p:productList) {
//			for(Size s:sizeList) {
//				
//				for(Temperature t:tempList) {
//					Product_option_info goods=new Product_option_info();
//					goods.setProduct(p);
//					goods.setSize(s);
//					goods.setTemperature(t);
//					
//					
//					 //가격설정
//					switch(s.getSizename()) {
//					case "None":
//					case "Small":
//					case "Tall":
//						
//						goods.setPrice(p.getPrice());
//						break;
//					case "Medium":
//					case "Grande":
//						
//						goods.setPrice(p.getPrice()+500);
//						
//						break;
//					case "Large":
//					case "Venti":
//						goods.setPrice(p.getPrice()+1000);
//						break;
//					default: 
//						System.out.println(s.getSizename());
//					}
//					
//					// 카테고리별 크기 설정
//					switch(goods.getProduct().getCategories().getCategoryName()) {
//					case "버거":
//					case "탄산":
//					case "사이드":
//					case "디저트":
//						
//						if(t.getTempname().equals("None")) {
//							
//							switch(s.getSizename()) {
//
//							case "Small":
//							case "Medium":
//							case "Large":
//							
//								OptionRepository.save(goods);
//								break;
//							}
//	
//						}
//						
//						break;
//						  
//					case "커피":
//						if(!(t.getTempname().equals("None"))) {
////							System.out.println(t.getTempname());
//							System.out.println(goods.getProduct().getCategories().getCategoryName());
//							switch(s.getSizename()) {
//
//							case "Tall":
//							case "Grande":
//							case "Venti":
//							
//								OptionRepository.save(goods);
//								break;
//							}
//	
//						}
//						
//						break;
//					
//						
//
//						}
//						 
//					}
//
//					
//				}
//				
//				
//			}
//			
//		}

//	@Test
//	void setoptionAdd() {
//
//
//
//		Iterable<Product> productList = ProductRepo.findAll();
//		Iterable<Size> sizeList = SizeRepository.findAll();
//		Iterable<Temperature> tempList = TemperRepo.findAll();
//		for(Product p:productList) {
//			for(Size s:sizeList) {
//
//				for(Temperature t:tempList) {
//					Product_option_info goods=new Product_option_info();
//					goods.setProduct(p);
//					goods.setSize(s);
//					goods.setTemperature(t);
//
//
//					 //가격설정
//					switch(s.getSizename()) {
//					case "None":
//					case "Small":
//					case "Tall":
//
//						goods.setPrice(p.getPrice());
//						break;
//					case "Medium":
//					case "Grande":
//
//						goods.setPrice(p.getPrice()+500);
//
//						break;
//					case "Large":
//					case "Venti":
//						goods.setPrice(p.getPrice()+1000);
//						break;
//					default:
//						System.out.println(s.getSizename());
//					}
//
//					// 카테고리별 크기 설정
//					switch(goods.getProduct().getCategories().getCategoryName()) {
//					case "세트":
//
//
//						if(t.getTempname().equals("None")) {
//
//							switch(s.getSizename()) {
//
//							case "Small":
//
//							case "Large":
//
//								OptionRepository.save(goods);
//								break;
//							}
//
//						}
//
//						break;
//
//
//						}
//
//					}
//
//
//				}
//
//
//			}
//
//		}
	
//	
//	
//	@Test
//	void imgeuplad2() {
//	      Product p1=ProductRepo.findById(1).get();
//	      Product p2=ProductRepo.findById(2).get();
//	      Product p3=ProductRepo.findById(3).get();
//	      ProductImage pi = new ProductImage();
//	      pi.setImageLoad("/img/"+p1.getProductName()+".jpg");
//	      pi.setImageName(p1.getProductName());
//	      pi.setProduct(p1);
//	      ProImgRepo.save(pi);
//	      
//	      ProductImage pi2 = new ProductImage();
//	      pi2.setImageLoad("/img/"+p2.getProductName()+".jpg");
//	      pi2.setImageName(p2.getProductName());
//	      pi2.setProduct(p2);
//	      ProImgRepo.save(pi2);
//	      
//	      ProductImage pi3 = new ProductImage();
//	      pi3.setImageLoad("/img/"+p3.getProductName()+".jpg");
//	      pi3.setImageName(p3.getProductName());
//	      pi3.setProduct(p3);
//	      ProImgRepo.save(pi3);
//	   
//	}
	
//	@Test
//	void imageupload() {
//		
//		
//		
//		Categories cate=CategoRepo.findByCategoryName("버거").get(0);
//		
//		
//		// 햄버거
//	     Product product = new Product();
//	     product.setProductName("불고기버거2");
//	     product.setPrice(3500);
//	     product.setAllSale(false);
//	     product.setCategories(cate);
//	     ProductRepo.save(product);
//	     
//	     String basepath="/img" + "/" + product.getProductName()+".jpg";
//	     
//	     ProductImage p_img=new ProductImage();
//         p_img.setImageName(product.getProductName());
//         p_img.setImageLoad(basepath);
//         p_img.setProduct(product);
//         ImageRepository.save(p_img);
//	     
//	      
//	}
	
//	void optionAdd() {
//	
//
//	
//	Iterable<Product> productList = ProductRepo.findAll();
//	Iterable<Size> sizeList = SizeRepository.findAll();
//	Iterable<Temperature> tempList = TemperRepo.findAll();
//	for(Product p:productList) {
//		for(Size s:sizeList) {
//			
//			for(Temperature t:tempList) {
//				Product_option_info goods=new Product_option_info();
//				goods.setProduct(p);
//				goods.setSize(s);
//				goods.setTemperature(t);
//				
//				
//				 //가격설정
//				switch(s.getSizename()) {
//				case "None":
//				case "Small":
//				case "Tall":
//					
//					goods.setPrice(p.getPrice());
//					break;
//				case "Medium":
//				case "Grande":
//					
//					goods.setPrice(p.getPrice()+500);
//					
//					break;
//				case "Large":
//				case "Venti":
//					goods.setPrice(p.getPrice()+1000);
//					break;
//				default: 
//					System.out.println(s.getSizename());
//				}
//				
//				// 카테고리별 크기 설정
//				switch(goods.getProduct().getCategories().getCategoryName()) {
//				case "버거":
//				case "탄산":
//				case "사이드":
//				case "디저트":
//					
//					if(t.getTempname().equals("None")) {
//						
//						switch(s.getSizename()) {
//
//						case "Small":
//						case "Medium":
//						case "Large":
//						
//							OptionRepository.save(goods);
//							break;
//						}
//
//					}
//					
//					break;
//					  
//				case "커피":
//					if(!(t.getTempname().equals("None"))) {
////						System.out.println(t.getTempname());
//						System.out.println(goods.getProduct().getCategories().getCategoryName());
//						switch(s.getSizename()) {
//
//						case "Tall":
//						case "Grande":
//						case "Venti":
//						
//							OptionRepository.save(goods);
//							break;
//						}
//
//					}
//					
//					break;
//				
//				case "세트":	
//					if(t.getTempname().equals("None")) {
//						
//						switch(s.getSizename()) {
//
//						case "Small":
//						
//						case "Large":
//						
//							OptionRepository.save(goods);
//							break;
//						}
//
//					}
//					
//					break;
//					}
//				 
//				
//					 
//				}
//
//				
//			}
//			
//			
//		}
//		
//	}
//
//}
}
