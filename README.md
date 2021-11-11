# SpringBootTest
JUnit5 is a Unit Test Framework.

To test specific task we need to write a test case(TestClass) with Annotations & Assertions.

=============================================================JUNIT 5 Annotations======================================================

JUNIT5 Annotations:-
===================


(1). @Test :-
==============
Specify this annotation on top of a test method

ex:-
===

@Test
public void addTest(){
    //addTestLogic
}

(2). @TestMethodOrder :-
=======================
* We can write multiple  test methods inside test class , those methods are executed random by default.
* we can specify our own order by using @TestMethodOrder at class level , @Order at method level.

case(1): Order(1),Order(10),Order(5) ===>ThenExecuton Order is 1,5,10
case(2): Order(1),Order(1), Order(1) ===>Random

Ex1:-
===
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Calculator {

	@Test
	@Order(9)
	public void addTest(){
		Calculater c=new Calculater();
		int actual  = c.add(10,20);
		int expected = 30;
		Assertions.assertEquals(actual,expected);
	}
	@Test
	@Order(6)
	public void multiplyTest(){
		Calculater c=new Calculater();
		int actual  = c.mul(10,20);
		int expected = 200;
		Assertions.assertEquals(actual,expected);
	}
	@Test
	@Order(3)
	public void subtractTest(){
		Calculater c=new Calculater();
		int actual  = c.subtract(10,20);
		int expected = -10;
		Assertions.assertEquals(actual,expected);
	}
}

(3). @BeforeEach :- To Execute logic once per each method Before starting it.
===================
Ex:-
====
@BeforeEach
public void initialSetUp(){
    System.out.println("initialsetup");
}

(4). @AfterEach :-To Execute logic once per each method After starting it.
=================
Ex:-
======
@AfterEach
public void initialSetUp(){
    System.out.println("finalsetup");
}

(5). @BeforeAll:-To Execute logic Only One time per all methods before starting it.
=================

Ex:-
@BeforeAll
public static  void initilsetUpBeoreAll(){
    System.out.println("Before All");
}

(6). @AfterAll:-To Execute logic Only One time per all methods After starting it.
================

Ex:-
@AfterAll
public static  void initilsetUpBeoreAll(){
    System.out.println("Before All");
}

(7). @DisplyName("METHOD OR CLASS NAME"):- This method can be used to specify display name in junit console.
========================================

Ex:-
@Test
@Order(3)
@DisplayName("TESTING SUBTRACT METHOD")
public void subtractTest(){
    Calculater c=new Calculater();
    int actual  = c.subtract(10,20);
    int expected = -10;
    Assertions.assertEquals(actual,expected);
}

(8). @Disabled:- this annotation is used to ignore a test method
================
@Test
@Order(9)
@Disabled
@DisplayName("TESTING ADD METHOD")
public void addTest(){
    Calculater c=new Calculater();
    int actual  = c.add(10,20);
    int expected = 30;
    Assertions.assertEquals(actual,expected);
}

(9). @RepetedTest:-
================== To Test a method specific number of times
Ex:-
@Test
@Order(3)
@RepeatedTest(value = 3)
@DisplayName("REPETED TEST FOR SUBTRACT METHOD")
public void subtractTest(){
    Calculater c=new Calculater();
    int actual  = c.subtract(10,20);
    int expected = -10;
    Assertions.assertEquals(actual,expected);
}

(10). @Tag("EnvironmentName"):-
==================================
 To Specify Execution Environment we can use this @Tag Annotation.
 
Ex1:-
@Test
@Order(6)
@Tag("prod")
@DisplayName("TESTING MULTIPLY METHOD")
public void multiplyTest(){
    Calculater c=new Calculater();
    int actual  = c.mul(10,20);
    int expected = 200;
    Assertions.assertEquals(actual,expected);
}
Ex2:
@Test
@Order(6)
@Tag("prod")
@DisplayName("TESTING MULTIPLY METHOD")
public void multiplyTest(){
    Calculater c=new Calculater();
    int actual  = c.mul(10,20);
    int expected = 200;
    Assertions.assertEquals(actual,expected);
}
Ex3:
@Test
@Order(6)
@Tag("qa")
@DisplayName("TESTING MULTIPLY METHOD")
public void multiplyTest(){
    Calculater c=new Calculater();
    int actual  = c.mul(10,20);
    int expected = 200;
    Assertions.assertEquals(actual,expected);
}

=======================================================JUNIT 5 Assertions================================================
We can use this to check actual values with expected values in testing




==========================================================================================================================
==========================================================================================================================
SpringBootTesting and Jacoco coverrage referes============================================================================
==========================================================================================================================
https://natritmeyer.com/howto/reporting-aggregated-unit-and-integration-test-coverage-with-jacoco/
