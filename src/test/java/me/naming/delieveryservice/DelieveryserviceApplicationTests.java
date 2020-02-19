package me.naming.delieveryservice;

import static org.hamcrest.CoreMatchers.is;

import me.naming.delieveryservice.dao.PaymentDao;
import me.naming.delieveryservice.dto.PaymentDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 테스트 클래스
 * SpringRunner.class vs SpringJUnit4ClassRunner.class 차이점
 *  -> 둘 차이점은 없다. SpringJUnit~은 SpringRunner 클래스에 상속된다
 *  -> 참고(https://stackoverflow.com/questions/47446529/what-is-the-difference-between-springjunit4classrunner-and-springrunner)
 *
 * 테스트 클래스 작성시 유념해야 할 사항
 *  - 반복적으로 테스트 했을 때 일관된 결과가 나와야 한다.
 *  - 동일한 결과를 보장해야 한다
 *
 * 테스트 코드를 작성한 후 실제 서비스 코드 작성하는 방법을 테스트 주도 개발(TDD)라고 한다.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DelieveryserviceApplicationTests {

    private PaymentDTO paymentDTO;
    @Autowired PaymentDao paymentDao;

    @Before
    public void testSetting(){
        paymentDTO = new PaymentDTO("계좌이체", 4500, 44, "빠른배송");
    }

    @Test
    public void contextLoads() {
        int result = paymentDao.paymentInfo(paymentDTO);
        Assert.assertThat(1, is(result));
        paymentDao.testDataDelete();
        int count = paymentDao.getCount();
        Assert.assertThat(1, is(count));
    }
}