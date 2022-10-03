package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.domain.Product;
import com.example.demo.domain.Product_option_info;
import com.example.demo.domain.Size;
import com.example.demo.domain.Temperature;
import com.example.demo.persistence.BasketRepository;
import com.example.demo.persistence.CategoriesRepository;
import com.example.demo.persistence.CustomerRepository;
import com.example.demo.persistence.OptionInfoRepo;
import com.example.demo.persistence.ProductImageRepository;
import com.example.demo.persistence.ProductRepository;
import com.example.demo.persistence.SizeRepository;
import com.example.demo.persistence.TemperRepository;
import com.example.demo.persistence.WorkerRepository;

@SpringBootTest
class FastfoodUpApplicationTests {

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

}
