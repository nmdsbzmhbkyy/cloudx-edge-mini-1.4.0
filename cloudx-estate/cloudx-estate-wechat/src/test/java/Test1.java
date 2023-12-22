import cn.hutool.core.date.DateUtil;
import com.aurine.cloudx.estate.util.QrcodeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Test1 {
    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        for (int i=0; i < 49; i++) {
            Random a=new Random();
            int aa=a.nextInt(1000000);
            String s=String.format("%o8",aa);
            data.add(s);
        }

        data.add("01010101");

        String info = QrcodeUtil.getInstance().convert(data);

        int now = (int) (System.currentTimeMillis() / 1000);
      String  code = QrcodeUtil.getInstance().getQrcode("何珊",
                "S" + "1000000696" + "-" + info, now,
                now+100000, 9999, 2);
        System.out.println(code);
        System.out.println(code.length());
    }
}
