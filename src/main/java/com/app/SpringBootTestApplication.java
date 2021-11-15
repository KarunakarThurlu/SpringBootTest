package com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class SpringBootTestApplication {
	/*@Autowired
	private IUserService service;

	@PostConstruct
	public void initUsers() {
		User u=new User();
		Set<Role> r=new HashSet<Role>();
		Role rr=new Role();
		rr.setRoleName("USER");
		r.add(rr);
		u.setRoles(r);
		u.setUserEmail("karun@gmail.com");
		u.setUserPwd("karun");
		u.setUserName("karun");
		service.saveUser(u);
	}*/
	public static void main(String[] args) {
		SpringApplication.run(SpringBootTestApplication.class, args);
	}

}












/*
 * 
 * 83f9b2e5aa90a231f7c3ce23f58f7ecb765fc8c6
  ./mvnw sonar:sonar
  -Dsonar.projectKey=SpringBootTest
  -Dsonar.host.url=http://localhost:9000
  -Dsonar.login=1e6c2f2cb249432aa903b48be26676823c2f329d
 * */






//https://books.google.co.in/books?id=dIvgDwAAQBAJ&lpg=PA391&ots=otOqVP19x1&dq=SharedSessionContractImplementor%20will%20close%20connection%20automatically%3F&pg=PA20#v=onepage&q&f=false