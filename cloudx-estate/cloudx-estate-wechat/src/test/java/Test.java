import com.aurine.cloudx.estate.util.QrcodeUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * (Test)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/25 9:57
 */
public class Test {


    public static void main(String[] args) {
        System.out.println("请输入加密内容");
        InputStreamReader is = new InputStreamReader(System.in); //new构造InputStreamReader对象
        BufferedReader br = new BufferedReader(is); //拿构造的方法传到BufferedReader中，此时获取到的就是整个缓存流
        try { //该方法中有个IOExcepiton需要捕获
            String name = br.readLine();
            String DeString = QrcodeUtil.getInstance().decrypt(name);
            System.out.printf("原内容为：" + DeString);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
