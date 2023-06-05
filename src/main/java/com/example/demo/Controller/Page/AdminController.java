package com.example.demo.Controller.Page;

import java.time.LocalDate;
import java.util.List;

//import com.example.demo.Service.AwsS3Service;
import com.example.demo.Service.*;
import com.example.demo.config.auth.jwt.UserData.CustomUserDetail;
import com.example.demo.dto.Response.*;
import com.example.demo.persistence.QueryFor.BestSellerNameAndCnt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

@Slf4j
@Controller // This means that this class is a Controller
@RequiredArgsConstructor
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
	private final CustomerService customerService;
	private final ManageService manageService;
	private final OrderService orderService;
	private final CategoriesService categoriesService;

	private final ProductService productService;

	@GetMapping("/superMainHome")
	public String adminHomeShow(Model model,@AuthenticationPrincipal CustomUserDetail customUser){
		log.info("어드민 페이지!!!");
         /* 어드민 메인페이지의 최상단의 4가지 박스(유저 수, 판매한 제품 수, 판매 총가격, 게시판 수 (이번 달) )와
		    저번달 유저의 수와 2개월 전~9개월 전까지 유저의 수를 구하는 메소드
		 */
		ManageReponseDto manageReponseDto=manageService.setData();

		//이번달에 가장 많이 제품의 수 (최대 7개)
		List<BestSellerNameAndCnt> bestSellerNameAndCntList=manageService.getBestSellerNameAndCnt();

		//현재 관리자의 정보를 담고있는 DTO
		CustomerResponseDto customerResponseDto =customerService.findById(customUser.getId());

		//현재 날짜(년.월)을 구하는 코드
		LocalDate now = LocalDate.now();
		int year=now.getYear();
		int month=now.getMonthValue();
		String currentDate=year+"."+month;

		//각 정보를 저장한 데이터를 전달
		model.addAttribute("customer",customerResponseDto);
		model.addAttribute("manage",manageReponseDto);
		model.addAttribute("bestSeller",bestSellerNameAndCntList);
		model.addAttribute("currentDate",currentDate);
		return "fastfood/admin/main/superMainHome";
	}

	//		if (yearMonth == null) {
//			LocalDate now = LocalDate.now();
//			year = now.getYear();
//			month = now.getMonthValue();
//		} else {
//			log.info(yearMonth);
//			String[] dateSplit = yearMonth.split("\\.");
//			year = Integer.parseInt(dateSplit[0]);
//			month = Integer.parseInt(dateSplit[1]);
//		}

	// Pageable 객체는 Pagination을 위한 정보를 저장하는 객체 (크기는 10이며 나중에 page 객체를 반환하는 jpa의 쿼리 메소드에서 limit가 10이 된다.)
	@GetMapping("/DaySettlement")
	public String showDaySettlement(@AuthenticationPrincipal CustomUserDetail customUser, Model model
			, @PageableDefault(size = 10) Pageable pageable, String yearMonth) {

		// Page 객체를 사용하면 전체 데이터 수, 전체 페이지를 알 수 있는 메소드를 지원 (slice와 차이점)
		// Pagination을 사용하기 떄문에 slice보다 page 객체를 사용
		Page<OrdersResponseDto> ordersDto = null;

		long totalPrice = 0;
		String currentDate = "";
		int year = 0, month = 0;

		//파라미터로 받아온 yearMonth(20xx.xx)를 '.' 기준으로 분리합니다.
		String[] dateSplit = yearMonth.split("\\.");
		year = Integer.parseInt(dateSplit[0]);
		month = Integer.parseInt(dateSplit[1]);

		//받아온 년도, 월 기준으로 ordersDto(Page객체)를 얻어옵니다.
		ordersDto = orderService.getOrdersInfo(year, month, pageable);
		//받아온 년도, 월 기준으로 총액을 얻어옵니다.
		totalPrice = orderService.getTotalPrice(month, year);
		//웹에서 현재 년도,월을 보여주기위해 합칩니다.
		currentDate = year + "." + month;
		//전달
		model.addAttribute("currentDate", currentDate);
		model.addAttribute("ordersDto", ordersDto);
		model.addAttribute("totalPrice", totalPrice);

		//웹의 Pagination를 위한 dto를 생성합니다.(현재 페이지 부터, 전체 페이지를 인자로 받습니다.)
		PageDto pageDto = orderService.makePageDto(ordersDto.getPageable().getPageNumber() + 1,
				ordersDto.getTotalPages());
		model.addAttribute("pages", pageDto);

		// 화살표 > 클릭 시 이동할 페이지 수를 정합니다.
		//기본은 현재 페이지 + 9입니다.
		int nextPage = pageDto.getCurrentPage() + 9;
		// nextPage가 최대페이지보다 클 시
		if (nextPage > ordersDto.getTotalPages()) {
			//마지막 페이지로 조정
			// -1을 한 이유는 배열 인덱스를 생각하면 된다.
			nextPage = pageDto.getMostEndPage() - 1;
		}
		model.addAttribute("nextPage", nextPage);

		//현재 로그인한 admin의 정보를 전달
		CustomerResponseDto customerResponseDto = customerService.findById(customUser.getId());
		model.addAttribute("customer", customerResponseDto);

		//현재월 ~ 지난 12개월 의 년도.월을 전달
		List<String> monthList = orderService.get12LastMonth();
		model.addAttribute("monthList", monthList);

		return "fastfood/admin/settle/DaySettlement";
	}

	@GetMapping("/productList")
	public String showProductList(@AuthenticationPrincipal CustomUserDetail customUser, Model model, String cateName) {
		//카테고리 리스트
		List<String> cateNameList=categoriesService.findCateNameList();
		//제품 정보 리스트
		String cname="";
		cname=cateName;
		List<ProductResponseDto> productDto=productService.findByCateName(cateName);

		CustomerResponseDto customerResponseDto =customerService.findById(customUser.getId());

		model.addAttribute("customer", customerResponseDto);
		model.addAttribute("cateNameList",cateNameList);
		model.addAttribute("productDto",productDto);
		return "fastfood/admin/product/productList";
	}

	@GetMapping(value="/productAdd")
	public String showProductAdd(@AuthenticationPrincipal CustomUserDetail customUser,Model model){
		CustomerResponseDto customerResponseDto =customerService.findById(customUser.getId());
		model.addAttribute("customer", customerResponseDto);
		return "fastfood/admin/product/productAdd";
	}

	@GetMapping("/productManage")
	public String showProductList(Model model, String productName,@AuthenticationPrincipal CustomUserDetail customUser){
		ProductResponseDto responseDto=productService.findByProductName(productName);
		List<String> cateNameList=categoriesService.findCateNameList();
		model.addAttribute("product",responseDto);
		model.addAttribute("cateNameList",cateNameList);

		CustomerResponseDto customerResponseDto =customerService.findById(customUser.getId());
		model.addAttribute("customer", customerResponseDto);

		model.addAttribute("id",productService.findIdByProductName(productName));
		return "fastfood/admin/product/productManage";
	}


}
