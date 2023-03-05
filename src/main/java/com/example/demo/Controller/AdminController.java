package com.example.demo.Controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.filechooser.FileSystemView;

//import com.example.demo.Service.AwsS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

@Controller // This means that this class is a Controller
@RequestMapping(path = "/fastfood") // This means URL's start with /demo (after Application path)
public class AdminController {
   @Autowired    // This means to get the bean called userRepository
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
   private ProductImageRepository ImageRepository;
   


	@Autowired
	private SizeRepository SizeRepository;
	@Autowired
	private OptionInfoRepo OptionRepository;
	@Autowired
	private TemperRepository TemperRepo;
	
	
	
	 @GetMapping("/superhome")
     public String adminView(Model model,HttpSession session){      
        LocalDate now = LocalDate.now();
        String[] datearray= now.toString().split("-");
        int month=Integer.parseInt(datearray[1]);
        System.out.println("현재 월: "+month+"월");
        model.addAttribute("currentmonth",month);
        return "fastfood/superhome";
      }
     @PostMapping("/superhome")
     public String adminMenu(Model model,HttpSession session){
        LocalDate now = LocalDate.now();
        String[] datearray= now.toString().split("-");
        int month=Integer.parseInt(datearray[1]);
        System.out.println("현재 월: "+month+"월");
        model.addAttribute("currentmonth",month);
        return "fastfood/superhome";
      }
	  @GetMapping("/DaySettlement")
     public String SettlementView(Model model,@RequestParam("month") String selectmonth) {
         Iterable<Orders> ordersList=OrdersRepo.findAll();
         ArrayList<Product> productList =new ArrayList<Product>();
         ArrayList<Customer> customerList =new ArrayList<Customer>();
         ArrayList<Orders> CurrentOrderList =new ArrayList<Orders>();
         int totalprice=0;
         int month=Integer.parseInt(selectmonth);      
         System.out.println("선택한 월: "+month);         //날짜 관련 
        LocalDate now = LocalDate.now();
        String[] datearray= now.toString().split("-");      
        String nowdate=datearray[0]+"년 "+datearray[1]+"월 "+datearray[2]+"일 ";
        int year=Integer.parseInt(datearray[0]);
        model.addAttribute("nowdate",nowdate);
        for(Orders o:ordersList) { //현재 날짜와 테이블 주문 날짜 비교
           
//           if(compareDate(year,month,o.getOrederDate())) { //테이블 데이터값이 현재 날짜와 같을 시
//              CurrentOrderList.add(o);
//           }
//           System.out.println(o.getOrederDate());
        }
         
         for(Orders o:CurrentOrderList) { 
            customerList.add(CustomerRepo.findById(o.getCustomer().getId()).get());
            productList.add(ProductRepo.findById(o.getProduct().getPid()).get());
            totalprice+=o.getPrice();
            
         }
         model.addAttribute("currentmonth",selectmonth);
         model.addAttribute("ordersList",CurrentOrderList);
         model.addAttribute("customerList",customerList);
         model.addAttribute("productList",productList);
         model.addAttribute("totalprice",totalprice);
        return "fastfood/DaySettlement";
     }
     
     
     public boolean compareDate(int year, int month, Date selectday) { //날짜 비교 함수
        String[] datearray= selectday.toString().split("-");
        int cyear=Integer.parseInt(datearray[0]);
        int cmonth=Integer.parseInt(datearray[1]);
        System.out.println("가져온 날짜: "+ selectday);
        System.out.println("뽑아온 데이터의 년: "+cyear+" 월: "+cmonth);
        if(year==cyear && month==cmonth) {
           return true;
        }
        return false;
     }

     
   @GetMapping("/productAdd")
   public String productAddView(Model model,HttpSession session){      
       Iterable<Categories> cateList=CategoriRepo.findAll();      
       model.addAttribute("cateList",cateList);   
      return "fastfood/productAdd";
    }
   

   
   
   @PostMapping("/productAdd")
   public String productAdd(Model model,HttpSession session, String pname, String pprice
         ,String category,@RequestParam("file") MultipartFile file) throws Exception{      
       Iterable<Categories> cateList=CategoriRepo.findAll();            
       model.addAttribute("cateList",cateList);
       int isaddSuccess=0;
       
      System.out.println("상품 이름: "+pname +" 상품 가격: "+pprice +" 상품 카테고리: "+category);
       String rootpath= FileSystemView.getFileSystemView().getDefaultDirectory().toString();
       String sts="\\workspace-spring-tool-suite-4-4.12.0.RELEASE\\FastFood\\src\\main\\resources\\static\\img\\";

       
       String basepath="C:\\Users\\home\\Documents\\workspace-spring-tool-suite-4-4.11.1.RELEASE\\fastfood_up\\src\\main\\resources\\static\\img" + "\\" + pname+".jpg";

      if(!pname.equals("")&&!pprice.equals("")) {//빈칸 검사
    	  if(ProductRepo.findByProductName(pname).isEmpty()) {
    		  File newFile=new File(basepath);
          file.transferTo(newFile); 
          System.out.println(basepath);
          Product product= new Product();
          product.setProductName(pname);
          product.setPrice(Integer.parseInt(pprice));   
             
          product.setCategories((Categories)(CategoriRepo.findByCategoryName(category).get(0)));        
          ProductRepo.save(product);
          isaddSuccess=1;
          
          ProductImage p_img=new ProductImage();
          p_img.setImageName(pname);
          p_img.setImageLoad(basepath);
          p_img.setProduct(product);
          ImageRepository.save(p_img);
          
          Iterable<Size> sizeList = SizeRepository.findAll();
      	  Iterable<Temperature> tempList = TemperRepo.findAll();
          
          for(Size s:sizeList) {
    			
    			for(Temperature t:tempList) {
    				Product_option_info goods=new Product_option_info();
    				goods.setProduct(product);
    				goods.setSize(s);
    				goods.setTemperature(t);
    				
    				
    				 //가격설정
    				switch(s.getSizename()) {
    				case "None":
    				case "Small":
    				case "Tall":
    					
    					goods.setPrice(product.getPrice());
    					break;
    				case "Medium":
    				case "Grande":
    					
    					goods.setPrice(product.getPrice()+500);
    					
    					break;
    				case "Large":
    				case "Venti":
    					goods.setPrice(product.getPrice()+1000);
    					break;
    				default: 
    					System.out.println(s.getSizename());
    				}
    				
    				// 카테고리별 크기 설정
    				switch(goods.getProduct().getCategories().getCategoryName()) {
    				case "버거":
    				case "탄산":
    				case "사이드":
    				case "디저트":
    					
    					if(t.getTempname().equals("None")) {
    						
    						switch(s.getSizename()) {

    						case "Small":
    						case "Medium":
    						case "Large":
    						
    							OptionRepository.save(goods);
    							break;
    						}

    					}
    					
    					break;
    					  
    				case "커피":
    					if(!(t.getTempname().equals("None"))) {
//    						System.out.println(t.getTempname());
    						System.out.println(goods.getProduct().getCategories().getCategoryName());
    						switch(s.getSizename()) {

    						case "Tall":
    						case "Grande":
    						case "Venti":
    						
    							OptionRepository.save(goods);
    							break;
    						}

    					}
    					
    					break;
    				
    				case "세트":	
    					if(t.getTempname().equals("None")) {
    						
    						switch(s.getSizename()) {

    						case "Small":
    						
    						case "Large":
    						
    							OptionRepository.save(goods);
    							break;
    						}
    					}
    					break;
    					}
    				}
    			}
          
          
          
          isaddSuccess=1;
          
    	  }
    	  else {
    		  isaddSuccess=0;
    		  System.out.println("중복된 상품입니다>>>>");
    	  }
    		  
    	  model.addAttribute("isaddSuccess",isaddSuccess);


      }                         
      return "fastfood/productAdd";
    }   
   
   @GetMapping("/supermenu")
	public String homepageView() {		
		System.out.println("@@GetMapping 메뉴 페이지  called...");				
		return "fastfood/supermenu";
	}
	
	@PostMapping("/supermenu")
	public String homepage( HttpSession session) {
		System.out.println(" @@PostMapping 메뉴 페이지 called...");		
		System.out.println("**************************************");			
		return "fastfood/supermenu";
	}

	@GetMapping("/edit")
	   public String editView(Model model,HttpSession session){      
	       Iterable<Categories> cateList=CategoriRepo.findAll();      
	       model.addAttribute("cateList",cateList);   
	      return "fastfood/edit";
	    }
	   

	   
	   
	   @PostMapping("/edit")
	   public String productEdit(Model model,HttpSession session, String pname, String pprice
	         ,String category,@RequestParam("file") MultipartFile file) throws Exception{      
	       Iterable<Categories> cateList=CategoriRepo.findAll();            
	       model.addAttribute("cateList",cateList);
	       int isaddSuccess=0;
	       
	      System.out.println("상품 이름: "+pname +" 상품 가격: "+pprice +" 상품 카테고리: "+category);

//		   AwsS3Service awsS3Service = new AwsS3Service();
//		   awsS3Service.setS3Client();
	       
	       String basepath="D:\\Project\\fastfood_up\\src\\main\\resources\\static\\img\\" +pname+".jpg";

	      if(!pname.equals("")&&!pprice.equals("")) {//빈칸 검사
	    	  if(ProductRepo.findByProductName(pname).isEmpty()) {
	    		  File newFile=new File(basepath);
	          file.transferTo(newFile); 
	          System.out.println(basepath);
	          Product product= new Product();
	          product.setProductName(pname);
	          product.setPrice(Integer.parseInt(pprice));   
	             
	          product.setCategories((Categories)(CategoriRepo.findByCategoryName(category).get(0)));        
	          ProductRepo.save(product);
	          isaddSuccess=1;
	    	  }
	    	  else {
	    		  System.out.println("중복된 상품입니다>>>>");
	    	  }
	    		  
	    	  model.addAttribute("isaddSuccess",isaddSuccess);


	      }                         
	      return "fastfood/edit";
	    }  
	   
	   @GetMapping("/productUpdate")
	   public String updateView(Model model){      
	      Iterable<Product> productList = ProductRepo.findAll();
	      model.addAttribute("productList", productList);
	      
	      Iterable<Categories> cateList = CategoriRepo.findAll();
	      model.addAttribute("cateList", cateList);
	      return "fastfood/productUpdate";
	    }
	   @PostMapping("/productUpdate")
	   public String updateMenu(Model model){     
	      Iterable<Product> productList = ProductRepo.findAll();
	      model.addAttribute("productList", productList);
	      
	      Iterable<Categories> cateList = CategoriRepo.findAll();
	      model.addAttribute("cateList", cateList);
	      return "fastfood/productUpdate";
	    }
	   
	   
	   @GetMapping("/productDetail")
	   public String productDetailG(Model model, @RequestParam("pid") int pid, Product product){
		   Iterable<Categories> cateList=CategoriRepo.findAll();            
	       model.addAttribute("cateList",cateList);  
	       Product p = ProductRepo.findById(pid).get();
		   model.addAttribute("product", p);
		   //ProductImage pimg = ImageRepository.findByP(pid).get();
//		   ProductImage pimg=ImageRepository.findByProduct(p).get(0);
		   //model.addAttribute("pimg", pimg); 
		   return "fastfood/productDetail";
		   
	   }
	   @PostMapping("/productDetail")
	      public String productDetailP(@RequestParam("pid") int pid,  Integer price, Model model,String allSale) {
	         System.out.println("품절여부: "+allSale);
	         
	         Product p = ProductRepo.findById(pid).get();                                                                                       
	         p.setPrice(price);
	         if(allSale!=null) {
	            p.setAllSale(true);
	            Soldout(p);
	         }
	         else {p.setAllSale(false);}
	         ProductRepo.save(p);                                                      
	         return "redirect:productUpdate";
	      }
	   
	      public void Soldout(Product p) {
	         Iterable<Basket> baketList = BasketRepo.findAll();
	         List<Product_option_info> pof = OptionRepository.findByProduct(p);
	         Boolean check = false;
	         for (Basket b : baketList) {
	            String[] array = b.getInfo().split(",");
	            check=false;
	            for (Product_option_info po : pof) {
	               if (check == false) {
	                  for (String id : array) {
	                     if (Integer.parseInt(id)==po.getInfoid()) {
	                        System.out.println("삭제");
	                        BasketRepo.delete(b);
	                        check = true;
	                        
	                     }
	                  }
	               }
	            }
	         }
	      }

	   @GetMapping("/ImageUpdate")
	   public String ImageUpdateG(Model model, @RequestParam("pid") int pid, Product product){
		   Iterable<Categories> cateList=CategoriRepo.findAll();            
	       model.addAttribute("cateList",cateList);  
	       Product p = ProductRepo.findById(pid).get();
		   model.addAttribute("product", p);
		   //ProductImage pimg = ImageRepository.findByP(pid).get();
		   ProductImage pimg=ImageRepository.findByProduct(p).get(0);
		   model.addAttribute("pimg", pimg); 
		   return "fastfood/ImageUpdate";
	   }
	   @PostMapping("/ImageUpdate")
	   public String onlyProduct(@RequestParam("pid") int pid,@RequestParam("file") MultipartFile file, String productName) 
			   throws IllegalStateException, IOException {
		   //ProductImage img=ImageRepository.findByProduct(p).get(0);
		   Product p = ProductRepo.findById(pid).get();

		   //s3 설정
//		   AwsS3Service awsS3Service = new AwsS3Service();
//		   awsS3Service.setS3Client();

		   String sts="D:\\Project\\fastfood_up\\src\\main\\resources\\static\\img\\";
		   String existpath=sts+p.getProductName()+".jpg";
		   System.out.println("이미지 저장 경로: "+existpath);
		   File existFile=new File(existpath);
//		   file.transferTo(existFile); 
		   if(existFile.exists()) {
			   System.out.println("이미지 저장 경로: "+existpath);
			   if(existFile.delete()) {
				   System.out.println("삭제 성공");
				   String imgname=p.getProductName();
				   String basepath=sts+imgname+".jpg";
				   File newFile=new File(basepath);
			       file.transferTo(newFile); 
			       ProductImage p_img=ImageRepository.findByProduct(p).get(0);
			       p_img.setImageName(imgname);
			       //p_img.setImageLoad(awsS3Service.upload(file, imgname));
			       p_img.setProduct(p);
			       ImageRepository.save(p_img); 
			   }
			   else { System.out.println("삭제 실패"); }
		   }
		   return "redirect:productUpdate";
	   }
	   @GetMapping("/delete")
	   public String delete(@RequestParam("pid") int pid) {
		   System.out.println("***********삭제***********");
		   Product p = ProductRepo.findById(pid).get();
	         List<Product_option_info>pof=OptionRepository.findByProduct(p);
	         ProductImage p_img=ImageRepository.findByProduct(p).get(0);	         
	         Iterable<Basket>baketList= BasketRepo.findAll();      
	         for(Product_option_info po:pof) {
	        	 System.out.println("***********옵션***********");
	        	 System.out.println(po.getInfoid());
	        	 
	        	 for(Basket b:baketList) {
	        		 if(b.getProductinfo().getInfoid()==po.getInfoid()) {
	        			 BasketRepo.delete(b);
	        		 }	        		 
	        	 }
        	 
	        	 OptionRepository.delete(po);
	         }
	         System.out.println("***********이미지   삭제***********");
	         ImageRepository.delete(p_img);
	         
	         System.out.println("***********상풍삭제***********");
	         ProductRepo.deleteProduct(p.getPid());
	         return "redirect:productUpdate";

	   }

	      @GetMapping("/CustomerManage")
	      public String manageView(Model model) {
	    	  System.out.println("====== @GetMapping(\"/CustomerManage\")========");
	         Iterable<Customer> customerList = CustomerRepo.findByRole(0);
	         model.addAttribute("customerList", customerList);
	         return "fastfood/CustomerManage";
	      }
	      @GetMapping("/cinfoUpdate")
	      public String customerinfoUpdateView(Model model, @RequestParam("cid") String cid) {
	         Customer c = CustomerRepo.findByUserId(cid).get(0);
	         model.addAttribute("customers", c);
	         return "fastfood/cinfoUpdate";
	      }
	      @PostMapping("/cinfoUpdate")
	      public String customerinfoUpdatePost(Model model, @RequestParam("cid") String cid, Customer customer) {
	         Customer c = CustomerRepo.findByUserId(cid).get(0);
	         c.setCardCompany(customer.getCardCompany());
	         c.setCardNum(customer.getCardNum());
	         c.setEmail(customer.getEmail());
	         c.setPhoneNum(customer.getPhoneNum());
	         c.setUserPasswd(customer.getUserPasswd());
	         c.setUserId(customer.getUserId());
	         CustomerRepo.save(c);
	         return "redirect:CustomerManage";
	      }
	      @GetMapping("/cinfoDelete")
	      public String customerinfoDelete(@RequestParam("cid") String cid) {
	         System.out.println("*****Customer 강제 삭제*****");
	         Customer c = CustomerRepo.findByUserId(cid).get(0);
	         Iterable<Basket> bList = BasketRepo.findByCustomer(c);
	         for(Basket b:bList) {
	            BasketRepo.delete(b);
	         }
	         CustomerRepo.delete(c);
	          return "redirect:CustomerManage";
	      }

}
