package com.example.demo.Controller;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.filechooser.FileSystemView;

import com.example.demo.Service.*;
import com.example.demo.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.domain.Basket;
import com.example.demo.domain.Categories;
import com.example.demo.domain.Customer;
import com.example.demo.domain.Orders;
import com.example.demo.domain.Product;
import com.example.demo.domain.ProductImage;
import com.example.demo.domain.Product_option_info;
import com.example.demo.domain.Size;
import com.example.demo.domain.Temperature;
import com.example.demo.domain.Worker;
import com.example.demo.persistence.BasketRepository;
import com.example.demo.persistence.CategoriesRepository;
import com.example.demo.persistence.CustomerRepository;
import com.example.demo.persistence.OptionInfoRepo;
import com.example.demo.persistence.OrdersRepository;
import com.example.demo.persistence.ProductImageRepository;
import com.example.demo.persistence.ProductRepository;
import com.example.demo.persistence.SizeRepository;
import com.example.demo.persistence.TemperRepository;
import com.example.demo.persistence.WorkerRepository;

import lombok.Getter;

@Controller // This means that this class is a Controller
@RequestMapping(path = "/fastfood") // This means URL's start with /demo (after Application path)
public class MainController {
	@Autowired 	// This means to get the bean called userRepository
	private BasketRepository BasketRepo;
	@Autowired
	private CategoriesRepository CategoriRepo;
	@Autowired
	private CustomerRepository CustomerRepo;
	@Autowired
	private OrdersRepository OrdersRepo;
	@Autowired
	private ProductRepository ProductRepo;
	@Autowired
	private WorkerRepository WorkerRepository;
	@Autowired
	private ProductImageRepository ProductImgRepo;

	@Autowired
	private SizeRepository SizeRepository;
	@Autowired
	private OptionInfoRepo OptionRepository;
	@Autowired
	private TemperRepository TemperRepo;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private CategoriesService categoriesService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductOptionService productOptionService;
	@Autowired
	private SizeService sizeService;
	@Autowired
	private TemperatureService temperatureService;
	@Autowired
	private BasketService basketService;

	static final String s3Url="https://fastfood-spring-build.s3.ap-northeast-2.amazonaws.com/img/";
	@GetMapping("/logout")
    public String logoutGet(HttpSession session, HttpServletRequest request) {
       session = request.getSession();
       session.invalidate();
       System.out.println("*******세션 초기화*******");
       System.out.println("*******로그아웃*******");
       return "fastfood/login";
    }

	 @GetMapping("/CustomerUpdate")
     public String cUpdate(Model model,Customer customer) {
        //Iterable<>
        return "fastfood/CustomerUpdate";
     }
     @PostMapping("/CustomerUpdate")
     public String cUpdateP(HttpSession session, CustomerRequestDto customer, @RequestParam("id") int id) {
        Customer c=CustomerRepo.findById(id).get();
//        c.setPhoneNum(customer.getPhoneNum());
//        c.setCardCompany(customer.getCardCompany());
//        c.setCardNum(customer.getCardNum());
//        c.setEmail(customer.getEmail());
//        c.setUserId(customer.getUserId());
//        c.setUserPasswd(customer.getUserPasswd());
//        CustomerRepo.save(c);
		customerService.updateUserInfo(id,customer);
        session.setAttribute("user", c);
        return "forward:fastfood/menu";
     }
	
	@GetMapping("/register")
	public String signupView() {
		return "fastfood/register";
	}
	
	@PostMapping("/register")
	public String signupSuccessView(CustomerRequestDto customer) {
		customerService.saveUserInfo(customer);
		return "redirect:fastfood/login";
	}
	
	@GetMapping("/login")
	public String loginView( ) {
		return "fastfood/login";
	}
	
	@PostMapping("/login")
	public String loginSuccessView(Model model, String userId, String userPasswd,HttpSession session)  {
		System.out.println("login controller");
		System.out.println(userId + ", " + userPasswd);
		boolean existUser=customerService.findUsers(userId,userPasswd);
		int isloginSuccess=0;
		if(existUser){
			isloginSuccess=1;
			//반드시 수정 필요
			session.setAttribute("user", CustomerRepo.findByUserId(userId).get(0));
			if(customerService.getUser(userId).getRole()==1){
				return "forward:/fastfood/superhome";
			}
			else
				return "forward:/fastfood/menu";
		}
		model.addAttribute("isloginSuccess", isloginSuccess);
		return "fastfood/login";
//		Iterable<Customer> cusList = CustomerRepo.findAll();
//		int isloginSuccess=0;
//		for (Customer c : cusList) {
//			if(userId.equals(c.getUserId()) && userPasswd.equals(c.getUserPasswd())) {
//				isloginSuccess=1;
//				System.out.println("로그인 결과값"+isloginSuccess);
//				if(c.getRole()==1) {
//					session.setAttribute("user", c);
//					return "forward:/fastfood/superhome";
//					}
//				else {
//					session.setAttribute("user", c);
//					return "forward:/fastfood/menu";
//					}
//			}
//		}
//
//		model.addAttribute("isloginSuccess", isloginSuccess);
//		System.out.println(isloginSuccess);
		
//		return "fastfood/login";
	}
	@GetMapping("/new_food")
	public String new_foodView() {		
					
		return "fastfood/new_food";
	}
	
	@PostMapping("/new_food")
	public String new_foodpage(String foodName,Integer foodPrice,String foodCate) {
		
		System.out.println(foodName);
		System.out.println(foodPrice);
		System.out.println(foodCate);
		
		Categories c =CategoriRepo.findByCategoryName(foodCate).get(0);
		System.out.println(c.getCategoryId());
		
		return "fastfood/new_food";
	}
	
	@GetMapping("/fileupload")
	public String fileupload() {		
					
		return "fastfood/file_upload";
	}

	@PostMapping("/fileupload")
	public String uploadSingle(@RequestParam("files") MultipartFile file) throws Exception {
	    String rootPath = FileSystemView.getFileSystemView().getHomeDirectory().toString();
	    System.out.println(rootPath);
	    String basePath = rootPath + "\\" + "media";
	    System.out.println(basePath);
	    String filePath = "C:\\Users\\home\\Documents\\workspace-spring-tool-suite-4-4.11.1.RELEASE\\fastfood_up\\src\\main\\resources\\static\\img" + "\\" + file.getOriginalFilename();
	    File dest = new File(filePath);
	    file.transferTo(dest); // 파일 업로드 작업 수행
	    System.out.println(filePath);
	    return "fastfood/file_upload";
	}
	
	@GetMapping("/menu")
	public String homepageView() {		
		System.out.println("@@GetMapping 메뉴 페이지  called...");				
		return "fastfood/menu";
	}
	
	@PostMapping("/menu")
	public String homepage( HttpSession session) {
		System.out.println(" @@PostMapping 메뉴 페이지 called...");		
		System.out.println("**************************************");			
		return "fastfood/menu";
	}

	 @GetMapping(value="/Hdetailmenu")
	    public String detailmenuView(Model model,@RequestParam("menuid") int menuid,@RequestParam("menuname") String menuname,HttpSession session){      
	      System.out.println(" @@GetMapping 상세메뉴 페이지 called...");   
//	      List<ProductImage> ProImgList=new ArrayList<ProductImage>();
	      //CategoriesDTO cate = categoriesService.getCategories(menuid);
	 
//	      List<Product> productList=categoriesService.getCategoriesInProductList(menuid);
//	      for(Product product : ProductRepo.findAll()) {
//	         if(product.getCategories().getCategoryId()==menuid) {
//	            productList.add(product);
//	            System.out.println("---> " + product.getProductName());
//	            System.out.println(product.getPId());
//
//	         }
//	      }
	      if (menuid == 6) {
	              //List<Categories> CatedrinkList=new ArrayList<Categories>();
	              //List<Categories> CatesideList=new ArrayList<Categories>();
//	              List<Product> drinkList=new ArrayList<Product>();
//	              List<Product> sideList=new ArrayList<Product>();
//	              List<Integer> dpList=new ArrayList<Integer>();
//	              List<Integer> spList=new ArrayList<Integer>();
	              //CatedrinkList=CategoriRepo.findByCategoryName("탄산");
	              //CatesideList=CategoriRepo.findbyDrinkMenu("사이드", "디저트");
//			  for (Product p : categoriesService.getFindSoda()) {
//				  drinkList.add(p);
//				  if (p.getPrice() <= 1500) {
//					  dpList.add(0);
//				  } else {
//
//					  dpList.add(p.getPrice() - 1200);
//				  }
//			  }
//			  for (Product p : categoriesService.getFindDessertAndSide()) {
//				  sideList.add(p);
//				  if (p.getPrice() <= 1500) {
//					  spList.add(0);
//				  } else {
//
//					  spList.add(p.getPrice() - 1500);
//				  }
//			  }
			  model.addAttribute("sideList", categoriesService.getFindDessertAndSide());
			  model.addAttribute("spList", productService.findSidePrice(categoriesService.getFindDessertAndSide()));
			  model.addAttribute("drinkList", categoriesService.getFindSoda());
			  model.addAttribute("dpList", productService.findDrinkPrice(categoriesService.getFindSoda()));

	     }
	       
	      model.addAttribute("productlist", categoriesService.getCategoriesInProductList(menuid));
	      model.addAttribute("goodslist",productOptionService.findAllOption());
	      model.addAttribute("menuid",menuid);      
	      model.addAttribute("menuname",menuname);
		  //model.addAttribute("s3url",s3Url);
	      return "fastfood/Hdetailmenu";
	    }


	@PostMapping(value="/Hdetailmenu")
    public String Hdetailmenu(@RequestParam("mid") int mid,Model model,HttpSession session,@RequestParam("pname") String pname,
          @RequestParam("size") String size,@RequestParam("temp") String temp){
      
      model.addAttribute("menuid",mid);
      System.out.println(" @@PostMapping homepageView called...");
      System.out.println("**********@PostMapping(\"/Hdetailmenu\")****************************");

      System.out.println("현재 메뉴 번호: " + mid);      
      System.out.println("구매한 제품: "+pname);
      System.out.println("크기: "+size);
      System.out.println("온도: "+temp);
      //int menuid=Integer.parseInt(mid);
      //얻어온 값으로 기본 값 구해서 option 테이블 참조해서 기본값 얻어오기
      if(mid!=6) {
//         Product save_p=ProductRepo.findByProductName(pname).get(0);
//         Size save_s=(SizeRepository.findBySizename(size).get(0));
//         Temperature save_t=TemperRepo.findByTempname(temp).get(0);
		  //1/21
		  ProductRequsetDto productDTO=productService.getProduct(pname);
		  SizeRequestDTO sizeDTO=sizeService.getSize(size);
		  TemperatureRequestDto temperatureDTO=temperatureService.getTemperature(temp);
		  ProductOptionInfoRequestDto productOptionInfoRequestDto=productOptionService.getOption(productDTO,sizeDTO,temperatureDTO);
		  basketService.saveSingleMenu(productOptionInfoRequestDto,session);

		 //고쳐야할 비즈니스 로직
//         Product_option_info save_o=OptionRepository.findOptionbyinfos(save_p, save_s, save_t).get(0);
//
//         System.out.println("상세아이디: " +   save_o.getInfoid());
//         System.out.println("크기: " +   save_o.getSize().getSizename());
//         System.out.println("온도: " +   save_o.getTemperature().getTempname());
//
//         Basket basket = new Basket();
//         basket.setProductinfo(save_o);
//         basket.setCustomer((Customer)session.getAttribute("user"));
//         basket.setPCount(1);
//         basket.setInfo(String.valueOf(save_o.getInfoid()));
//         basket.setPrice(save_o.getPrice());
//         BasketRepo.save(basket);
      }
      
      else {
    	  ProductOptionInfoRequestDto[] save_o=new ProductOptionInfoRequestDto[3];
    	  ProductRequsetDto[] save_p=new ProductRequsetDto[3];
    	  String[] setpanme=pname.split(",");
    	  int price= Integer.parseInt(setpanme[3]);
    	  
    	  for(int i=0;i<3;i++) {
			  save_p[i]=productService.getProduct(setpanme[i]);
			  SizeRequestDTO sizeRequestDTO=sizeService.getSize(size);
			  TemperatureRequestDto temperatureRequestDto=temperatureService.getTemperature(temp);
			  save_o[i]=productOptionService.getOption(save_p[i],sizeRequestDTO,temperatureRequestDto);
//    		  save_p[i]=ProductRepo.findByProductName(setpanme[i]).get(0);
//    		  Size save_s=(SizeRepository.findBySizename(size).get(0));
//              Temperature save_t=TemperRepo.findByTempname(temp).get(0);
//              save_o[i]=OptionRepository.findOptionbyinfos(save_p[i], save_s, save_t).get(0);
//              System.out.println("상세아이디: " +   save_o[i].getInfoid());
//              System.out.println("크기: " +   save_o[i].getSize().getSizename());
//              System.out.println("온도: " +   save_o[i].getTemperature().getTempname());
    	  }

    	  //String info=save_o[0].getInfoid()+","+save_o[1].getInfoid()+","+save_o[2].getInfoid();
		  String info=productOptionService.infotoString(save_o);
		  basketService.saveSetMenu(save_o[0],session,info);
//          Basket basket = new Basket();
//          basket.setProductinfo(save_o[0]);
//          basket.setCustomer((Customer)session.getAttribute("user"));
//          basket.setPCount(1);
//          basket.setInfo(info);
//          basket.setPrice(price);
//          BasketRepo.save(basket);

      }

    return null;
   }
	
	
	@GetMapping("/mybasket")
    public String cartView(Model model,HttpSession session){      
//		Iterable<Basket> basketList = BasketRepo.findByCustomer((Customer)session.getAttribute("user"));
//		for(Basket b:basketList) {
//			System.out.println(b);
//		}
		//	Iterable<Basket> basketList = BasketRepo.find();
//		model.addAttribute("s3url",s3Url);
      return "fastfood/my_baket";
    }
	
	@PostMapping("/mybasket")
    public String cartPost(Model model,HttpSession session){ 
		System.out.println(" @@PostMapping ShoppingCarteView called...");
	      System.out.println("**********@PostMapping(\"/ShoppingCart\")****************************");
		List<Basket> basketList = BasketRepo.findByCustomer((Customer)session.getAttribute("user"));
		ArrayList<Product> productList =new ArrayList<Product>();
		ArrayList<Product_option_info> productInfoList =new ArrayList<Product_option_info>();
		ArrayList<Integer> priceList =new ArrayList<Integer>();			
		int productcount=0;
		
		//장바구니 테이블 수량 업데이트
		for(int i=0;i<basketList.size(); i++) {
			
			for(int j=i+1; j<basketList.size(); j++) {	
				
				if(basketList.get(i).getInfo().equals(basketList.get(j).getInfo())) { //아이템이 같을 시
					if(basketList.get(i).getCustomer().getId()==basketList.get(i).getCustomer().getId()){
						basketList.get(i).setPCount(basketList.get(i).getPCount()+1); //수량 증가
						BasketRepo.save(basketList.get(i)); // 정보 변경
						BasketRepo.deleteById(basketList.get(j).getBid()); //중복된 아이템 테이블에서 제거
						basketList.remove(j); //중복된 아이템 리스트에서 제거
					}
										
				}
			}
		}
		
		//제품 리스트, 가격 리스트 생성
		for(Basket b:basketList) {
			productInfoList.add(OptionRepository.findById(b.getProductinfo().getInfoid()).get());
			priceList.add(OptionRepository.findById(b.getProductinfo().getInfoid()).get().getPrice()*b.getPCount());
			
			productcount += b.getPCount();
		}
		int totalprice=0;
		for(int i=0; i<priceList.size(); i++) {
			totalprice+=priceList.get(i);
		}
		
		Map<Integer, String[] >map=new HashMap<>();
		
		for(Basket b:basketList) {
			
			if(b.getProductinfo().getProduct().getCategories().getCategoryName().equals("세트")) {
				
			String[] setpanme=b.getInfo().split(",");
			
			String[] sidemenu= {OptionRepository.findById(Integer.parseInt(setpanme[1])).get().getProduct().getProductName()
								,OptionRepository.findById(Integer.parseInt(setpanme[2])).get().getProduct().getProductName()};
			map.put(b.getBid() , sidemenu);
			}
			
			productInfoList.add(OptionRepository.findById(b.getProductinfo().getInfoid()).get());
			priceList.add(OptionRepository.findById(b.getProductinfo().getInfoid()).get().getPrice()*b.getPCount());
			
			
		}
		model.addAttribute("basketMap",map);
		model.addAttribute("productInfoList",productInfoList);
		model.addAttribute("basketList",basketList); // html each문 반복횟수
		model.addAttribute("priceList",priceList); 
		model.addAttribute("productList",productList);
		model.addAttribute("totalprice",totalprice);
		model.addAttribute("productcount",productcount);
		model.addAttribute("s3url",s3Url);
      return "fastfood/my_baket";
    }
	   
	@ResponseBody
	@PostMapping("/basket_save")
	public String basket_save(@RequestParam(value = "basketarray[]") List<String> basketarray, HttpSession session,
			@RequestParam(value = "counts[]") List<String> counts, @RequestParam("checkpage") int checkpage)
			throws Exception {
		System.out.println(" @@PostMapping basket_saveView called...");
		ArrayList<Basket> basketList = new ArrayList<Basket>();
		Iterable<Basket> deleteList = BasketRepo.findByCustomer((Customer) session.getAttribute("user"));
		
		System.out.println("************변경된 바켓 객체 저장**************");
		for (String b : basketarray) { //변경된 바켓 객체 저장
			basketList.add(BasketRepo.findById(Integer.parseInt(b)).get());
			System.out.println(b);
		}
		for (Basket b : deleteList) { //해당 손님의 장바구니 아이템 전체 삭제
			BasketRepo.delete(b);
		}	
		
		
		for (int i = 0; i < basketList.size(); i++) { //다시 가져온 바켓 객체 데베에 삽입
			Basket newbasket = new Basket();
			newbasket = basketList.get(i);
			newbasket.setPCount(Integer.parseInt((counts.get(i))));
			System.out.println("바스켓 아이디: " + newbasket.getBid() + " 바스켓 수량: " + newbasket.getPCount());
			BasketRepo.save(newbasket);
		}
		String message="AJAX 성공";
		return message;
	}
	
	
	 @GetMapping("/Payment")
	   public String paymentView(HttpSession session,Model model) {
	      System.out.println(" @@GetMapping 결제페이지 called...");
	      Iterable<Basket> basketList = BasketRepo.findByCustomer((Customer) session.getAttribute("user"));
	      
	      int total=calcu_price(basketList);
	      System.out.println("%%%%%%%%%%%%%"+total+"%%%%%%");
	      model.addAttribute("id",((Customer)session.getAttribute("user")).getUserId());
	      
	      model.addAttribute("totalprice", total);
	      return "fastfood/Payment";
	            
	   }
	   @PostMapping("/Payment")
	   public String payment(HttpSession session,Model model,String cardCompany, String cardNumber) {
	      System.out.println(" @@PostMapping 결제페이지 called...");
	      System.out.println("가져온 값: "+cardCompany+cardNumber);
	      Iterable<Basket> basketList = BasketRepo.findByCustomer((Customer) session.getAttribute("user"));
	      Customer c=(Customer)session.getAttribute("user");
	      int total=calcu_price(basketList);
	      model.addAttribute("id",c.getUserId());
	      model.addAttribute("totalprice", total);
	      int isCheck=0;
	            
	      if(!cardNumber.equals("")&&!cardCompany.equals("")) {//빈칸 X         
	         int checkResult=checkUserPayInfo(cardNumber,cardCompany,
	               (Customer)session.getAttribute("user")); //체크함수
	         switch(checkResult) {
	         case 1:
	            isCheck=1;
	            break;
	         case -1:
	            isCheck=-1;
	            break;
	         case -2:
	            isCheck=-2;
	            break;
	         }
	         model.addAttribute("check",isCheck);   
	      }
	      else { //빈칸 O
	        isCheck=-3;   
	        model.addAttribute("check",isCheck);   
	      }
	         
	      if(isCheck==1) {               
	         for(Basket b:basketList) {
	            Orders o=new Orders();
	            o.setCustomer(c);
	            
	            
	            o.setProduct(b.getProductinfo().getProduct());
	            o.setPrice(b.getPrice());
	            o.setInfo(b.getInfo());
	            
	            o.setOrederDate(new Date());
	            OrdersRepo.save(o);
	            BasketRepo.delete(b);
	         }         
	         return "fastfood/Payment";
	      }
	      return "fastfood/Payment";
	   }
	   public int checkUserPayInfo(String cardnum, String cardCompany,Customer customer) {
	      if(!cardnum.equals(customer.getCardNum())) {
	         return -1;
	      }
	      if(!cardCompany.equals(customer.getCardCompany())) {
	         return -2;
	      }
	      return 1; //성공
	   }
	   public int calcu_price(Iterable<Basket> basketList) {
	      int totalprice=0;
	      for(Basket b:basketList) {
	    	  
	         int price=b.getProductinfo().getPrice();
	         price=price*b.getPCount();
	         totalprice+=price;
	      }
	      
	      return totalprice;
	   }
	
}