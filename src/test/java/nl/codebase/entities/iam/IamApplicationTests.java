package nl.codebase.entities.iam;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IamApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void byteToChar() {
		char[] chars = "XY7kmzoNzl100".toCharArray();
		for (char aChar : chars) {
			byte b = (byte) aChar;
		}
	}

}
