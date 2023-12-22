import com.aurine.cloudx.estate.util.QrcodeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Test2 {
    public static void main(String[] args) {
        List<String> data = new ArrayList<>();

        data.add("01010101");

        String info = QrcodeUtil.getInstance().convert(data);

        int now = (int) (System.currentTimeMillis() / 1000);
      String  code = QrcodeUtil.getInstance().getQrcode("何珊",
                 "1000000962" + "-" + info, now,
                now+100000, 9999, 2);
        System.out.println(code);
        System.out.println(code.length());
    }
}
