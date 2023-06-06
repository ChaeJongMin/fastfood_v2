
package com.example.demo.persistence;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Basket;
import com.example.demo.domain.Customer;
import com.example.demo.domain.Product;
import com.example.demo.domain.Size;
import com.example.demo.domain.Temperature;
import com.example.demo.domain.Product_option_info;

public interface OptionInfoRepo extends JpaRepository<Product_option_info, Integer> {
	public List<Product_option_info> findByProduct(Product searchword);
	public List<Product_option_info> findBySize(Size searchword);
	public List<Product_option_info> findByTemperature(Temperature searchword);
	
	public void deleteByInfoid(int id);
	
	@Modifying
	@Query("select b from Product_option_info b where b.product=:product and b.size=:size and b.temperature=:temperature")
	public Product_option_info findOptionbyinfos(Product product, Size size, Temperature temperature);

	public Product_option_info findByProductAndSizeAndTemperature(Product product, Size size, Temperature temperature);

	public Product_option_info findByProduct_PidAndSize_SidAndTemperature_Tid(int pid, int sid, int tid);

	public void deleteAllByProduct(Product product);
}


