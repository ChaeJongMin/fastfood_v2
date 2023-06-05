package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.persistence.*;
import com.example.demo.persistence.ConnectCustomerRepository;
import com.example.demo.persistence.QueryFor.VisitorSummary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations={"spring-servlet.xml"})
//
//@SpringBootTest(classes = {WorkerRepository.class, Worker.class}) 
//@SpringBootApplication(scanBasePackages={"com.example.persistence.WorkerRepository"})
////@RunWith(WorkerRepository.class)
////@ContextConfiguration(classes = {WorkerRepository.class, Worker.class}) 
//@EntityScan(basePackages = {"com.example.domain"})

@SpringBootTest
class QueryMethodTest {

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
	private ConnectCustomerRepository connectCustomerRepository;
	@Autowired
	private OptionInfoRepo OptionRepository;
	@Autowired
	private OrdersRepository ordersRepository;
	@Autowired
	private BoardsRepository boardsRepository;

	@Test
	public void testQuery(){
//		long totalBoard=boardsRepository.countCurrentMonthBorads(5,2023);
//		System.out.println("총 게시판 수(월): "+totalBoard);
//		//총 주문 수
//		long totalCnt=ordersRepository.countCurrentByMonthOrders(5,2023);
//		System.out.println("총 주문 수(월): "+totalCnt);
//		//총 판매액
//		long totalSale=ordersRepository.sumCurrentPriceByMonthOrders(5,2023);
//		System.out.println("총 판매액(월): "+totalSale);
//		//베스트 셀러
//		List<BestSellerNameAndCnt> list1=ordersRepository.findBestSellerNameAndCnt(5,2023);
//		System.out.println("---------------베스트셀러: "+list1.size()+" ---------------");
//		for(BestSellerNameAndCnt bsnac : list1 ){
//			System.out.println(bsnac.getName()+" "+bsnac.getCnt());
//		}
//		System.out.println("-----------------------------------------------------");
//		// 카테고리별 구매한 제품 수
//		List<CategoryNameAndCnt> list2=ordersRepository.findCategoriesNameAndCnt();
//		System.out.println("---------------카테고리별: "+list2.size()+" ---------------");
//		for(CategoryNameAndCnt cnac : list2){
//			System.out.println(cnac.getName()+" "+cnac.getCnt());
//		}
//		System.out.println("-----------------------------------------------------");
//		List<OrderSummary> list=ordersRepository.getOrderSummaries();
//		for(OrderSummary o : list){
//			System.out.println(o.getYearMonth()+" "+o.getTotal());
//		}
		long value=connectCustomerRepository.getPastUser();
		System.out.println(value);
	}

//	@Test
//	public void testInsertProduct() {
//        String str_cate[]= {"버거","탄산","커피","사이드","디저트"};
//        Categories cate[]=new Categories[5];
//        for (int i = 0; i < 5; i++) {
//           cate[i]=new Categories();
//           cate[i].setCategoryName(str_cate[i]);
//         CategoRepo.save(cate[i]);
//        }
//     // 햄버거
//     Product product = new Product();
//     product.setProductName("불고기버거");
//     product.setPrice(3500);
//     product.setAllSale(false);
//     product.setCategories(cate[0]);
//     ProductRepo.save(product);
//
//     product = new Product();
//     product.setProductName("새우버거");
//     product.setPrice(3000);
//     product.setAllSale(false);
//     product.setCategories(cate[0]);
//     ProductRepo.save(product);
//     
//     //커피
//     product = new Product();
//     product.setProductName("아메리카노");
//     product.setPrice(1500);
//     product.setAllSale(false);
//     product.setCategories(cate[2]);
//     ProductRepo.save(product);
//     
//     product = new Product();
//     product.setProductName("카페라떼");
//     product.setPrice(2000);
//     product.setAllSale(false);
//     product.setCategories(cate[2]);
//     ProductRepo.save(product);
//     
//     //탄산
//     product = new Product();
//     product.setProductName("콜라");
//     product.setPrice(1200);
//     product.setAllSale(false);
//     product.setCategories(cate[1]);
//     ProductRepo.save(product);
//     
//     product = new Product();
//     product.setProductName("사이다");
//     product.setPrice(1200);
//     product.setAllSale(false);
//     product.setCategories(cate[1]);
//     ProductRepo.save(product);
//     
//     //사이드
//     product = new Product();
//     product.setProductName("감자튀김");
//     product.setPrice(1500);
//     product.setAllSale(false);
//     product.setCategories(cate[3]);
//     ProductRepo.save(product);
//     
//     product = new Product();
//     product.setProductName("치킨너겟");
//     product.setPrice(1500);
//     product.setAllSale(false);
//     product.setCategories(cate[3]);
//     ProductRepo.save(product);
//     
//     //디저트
//     product = new Product();
//     product.setProductName("소프트아이스크림");
//     product.setPrice(700);
//     product.setAllSale(false);
//     product.setCategories(cate[4]);
//     ProductRepo.save(product);
//     
//     product = new Product();
//     product.setProductName("츄러스");
//     product.setPrice(1000);
//     product.setAllSale(false);
//     product.setCategories(cate[4]);
//     ProductRepo.save(product);
//        }
	
//  @Test
//  public void testInsertCustomer() {
//	  Customer c=new Customer();
//      c.setUserId("super");
//      c.setUserPasswd("123");
//      c.setEmail(c.getUserId()+"@naver.com");
//      c.setCardCompany("카카오뱅크");
//      c.setCardNum("333311000000");
//      c.setPhoneNum("010-0000-000");
//      c.setRole(1);
//      CustomerRepo.save(c);
//  }

//  @Test
//  public void testInsertCustomer() {
//     for(int i=0;i<5;i++) {
//        Customer c=new Customer();
//        c.setUserId("abcde"+i);
//        c.setUserPasswd("abcde"+i);
//        c.setEmail(c.getUserId()+"naver.com");
//        c.setCardCompany("카카오뱅크");
//        c.setCardNum("333311000000"+i);
//        c.setPhoneNum("010-0000-000"+i);
//        CustomerRepo.save(c);
//     }
//  }

	
//	  @Test
//	  public void fktest() {
//		  System.out.println("++++++++++++++++++++++++++++");
//		  
//		  ProductRepo.findByProductName("불고기버거").get(0).getInfo("Small", "None").getInfoid();
		  
		  
//		  System.out.println((ProductRepo.findByProductName("불고기버거").get(0)).getInfo("Small", "None").getInfoid());
//		  ProductRepo.findByProductName("불고기버거").get(0).getInfo("Small", "Hot").getSize()
//		  (Product_option_info)ProductRepo.findByProductName("불고기버거").get(0).getInfo("Small", "Hot")
//		  	Basket basket=new Basket();
//		  	basket.setPCount(2);
//		  	basket.setProductinfo(null);
////		  	basket.setProduct(ProductRepo.findByProductName("불고기버거").get(0));
//		  	basket.setCustomer(CustomerRepo.findByUserId("20173349").get(0));
//		  	basketRepo.save(basket);
//	  }
	  
//
//	@Test
//	public void inputimage() {
//		ProductImage pdi=new ProductImage();
//		ProductImage pdi2=new ProductImage();
//		pdi.setImageName("불고기버거");
//		pdi.setImageLoad("/img/bulgogibuger.jpg");
//		pdi.setProduct(ProductRepo.findById(1).get());
//		ProImgRepo.save(pdi);
//		
//		pdi2.setImageName("새우버거");
//		pdi2.setImageLoad("/img/shrimpbuger.jpg");
//		pdi2.setProduct(ProductRepo.findById(2).get());
//		ProImgRepo.save(pdi2);
//		
//	}
//}

	//테스트 실패
//	@Test
//	public void find() {
//		Categories cate =new Categories();
//		cate=CategoRepo.findById(1).get();
//		List<Product> productlist=ProductRepo.findByCategoryId(1);
//		for(Product p: productlist) {
//			System.out.println(p.toString());
//		}
//	}
	  
//	  @Test
//	  public void setadd() {
//
//
//	     // 햄버거
//	     Product product = new Product();
//	     product.setProductName("1955버거 세트");
//	     product.setPrice(0);
//	     product.setAllSale(false);
//	     product.setCategories(CategoRepo.findById(6).get());
//	     ProductRepo.save(product);
//
//
//
//
//	        }
}

