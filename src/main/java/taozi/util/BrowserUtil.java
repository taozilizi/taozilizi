package taozi.util;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import taozi.constant.Cons;
import com.github.wycm.biz.constant.UrlConstant;
import taozi.vo.AcceptorExlImpVo;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v111.network.Network;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
/***
 *
 可以操作多页
 * ****/
public class BrowserUtil {

    private static ChromeDriver driver;

    private static DevTools devTools;
    /**谷歌驱动*/
   // private static String chromeDriver="chromedriver.exe";
    /**opencv驱动*/
    //public static String dllPath = "D://opencv_java460.dll";


    static {
        System.setProperty("webdriver.chrome.whitelistedIps", "");
       // String path = System.getProperty("user.dir") + File.separator + chromeDriver;
        String path = UrlConstant.chromedriver;
        System.out.println(path);
        System.setProperty("webdriver.chrome.driver", path);
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);//缓存策略
        //chromeOptions.setBinary("E:\\demo\\Application\\chrome.exe");
        driver = new ChromeDriver(chromeOptions);
        devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();
        try {
            cfgChromeTool();
        } catch (Exception e) {
            System.err.println("cfgChromeTool报错:"+ Cons.current_vo);
        }
    }
    //初始化谷歌浏览器开发工具
    public static void cfgChromeTool(){
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        devTools.addListener(Network.responseReceived(), res -> {
            String url = "";
            JSONObject data = null;
            try {
                url = res.getResponse().getUrl();
                Thread.sleep(3000);//等待不然下面的responseBody可能拿不到
                String responseBody = devTools.send(Network.getResponseBody(res.getRequestId())).getBody();
                if(StringUtils.isNoneBlank(responseBody)&&responseBody.contains("code")&&responseBody.contains("success")){
                    System.out.println(DateUtil.getDate()+"--"+Cons.current_vo+"---成功进入转responseBody");
                    data = JSON.parseObject(responseBody);
                }
            } catch (Exception e) {
                System.err.println("转responseBody报错:"+Cons.current_vo);
            }
            if(url.contains(UrlConstant.url_findAccInfoListByAcptName)){
                System.out.println(DateUtil.getDate()+"--"+Cons.current_vo+"---url-base---"+url);
                HttpWatchUtil.findAccInfoListByAcptName(data);
            }else if(UrlConstant.url_slider_check.equals(url)){
                System.out.println(DateUtil.getDate()+"---"+Cons.current_vo+"---url-check---"+url);
                HttpWatchUtil.sliderCheck(data);
            }else if(UrlConstant.url_slider.equals(url)){
                System.out.println(DateUtil.getDate()+"---"+Cons.current_vo+"---urlslider----"+url);
                HttpWatchUtil.slider(data);
            }else if(url.contains(UrlConstant.url_findSettlePage)){
                System.out.println(DateUtil.getDate()+"----"+Cons.current_vo+"---url-detail---"+url);
                HttpWatchUtil.findSettlePageMulti(data);
            }

        });
    }

    public static void invoke(AcceptorExlImpVo vo) throws Exception{
        System.out.println("----------invoke  判断---是否导出数据成功----------"+Cons.allRegSuccess.contains(Cons.current_vo));
        if(Cons.allRegSuccess.contains(Cons.current_vo)){
            throw new Exception("成功查询详情"+Cons.current_vo);
        }

        System.out.println(DateUtil.getDate()+"--------------开始------------------------"+vo+"-----------------------开始----------------------");
        //设置input参数
        driver.get(UrlConstant.INDEX_URL);
        Thread.sleep(3000);
        //选择 点日期//有设置月份才点击日期框选择，没设置默认上个月
        if(StringUtils.isNotBlank(Cons.yearMonth)){
            clickDateInput(driver);
        }

        fillAcceptor(driver,vo);
        Thread.sleep(2000);
        //
        clickFindBtn();
        Thread.sleep(2000);

        slideBtn();
    }
    //选择信息时点日期
    public static void clickDateInput(WebDriver driver)throws Exception{
        By dateInput = By.cssSelector("div.el-date-editor--month > input.el-input__inner");//滑动验证按钮
        WebElement e = driver.findElement(dateInput);
        e.click();
        Thread.sleep(500);
        clickYear(driver);
        clickMonth(driver);
    }
    //选择日期控件---年
    public static void clickYear(WebDriver driver)throws Exception{
        By by = By.cssSelector("div.el-date-picker__header--bordered > span.el-date-picker__header-label");//滑动验证按钮
        WebElement yearSpan = driver.findElement(by);
        yearSpan.click();
        Thread.sleep(1000);
        Actions actions=new Actions(driver);
        actions.click(yearSpan).perform();
        System.out.println("-------VarConstant.year-----"+DateUtil.getYear(Cons.yearMonth)+"-----");
        List<WebElement> trs = driver.findElements(By.cssSelector(".el-year-table>tbody>tr"));//四层tr
        for (WebElement tr : trs) {
            List<WebElement> tds=  tr.findElements(By.tagName("td"));//四列td
            for (WebElement td : tds) {
                if(td.getText().equals(DateUtil.getYear(Cons.yearMonth))){
                    System.out.println("----year---td----"+td.getText()+"--------a---------");
                    td.click();
                    return;
                }
            }
        }
        Thread.sleep(500);
    }
    //选择日期控件--月
    public static void clickMonth(WebDriver driver)throws Exception{
        System.out.println("-------VarConstant.month-----"+DateUtil.getMonth(Cons.yearMonth)+"-----");
        List<WebElement> trs = driver.findElements(By.cssSelector(".el-month-table>tbody>tr"));//四层tr
        for (WebElement tr : trs) {
            List<WebElement> tds=  tr.findElements(By.tagName("td"));//四列td
            for (WebElement td : tds) {
                if(td.getText().equals(DateUtil.getMonth(Cons.yearMonth))){
                    System.out.println("----month---td----"+td.getText()+"---1111---找到了---------");
                    td.click();
                    return;
                }

            }
        }
        Thread.sleep(500);

    }
    /**
     * 等待元素加载，10s超时
     *
     * @param driver
     * @param by
     */
    public static void waitForLoad(final WebDriver driver, final By by) {
        new WebDriverWait(driver, Duration.ofSeconds(60)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                WebElement element = driver.findElement(by);
                if (element != null) {
                    return true;
                }
                return false;
            }
        });
    }


    //给填值
    public static void fillAcceptor(WebDriver driver,AcceptorExlImpVo vo) throws Exception {
        WebElement e = driver.findElement(By.xpath("//input[@valuekey='entName']"));
        e.clear();
        e.sendKeys(vo.getAcptName());
        e.click();
        //可根据加载网速调整
        Thread.sleep(5000);
        System.out.println(DateUtil.getDate()+"-----查找完成----- ");
        if(Cons.allUnReg.contains(vo)){//查询不到跑错，去查下一个
            System.err.println("fillAcceptor------未注册------报错:"+Cons.current_vo);
            throw new Exception("---成功查询未注册---"+Cons.current_vo);
        }
        // 方法一：获取所有下拉框元素,通过list索引选择下拉框元素
        Actions action=new Actions(driver);
        action.moveToElement(e).perform();
        Thread.sleep(2000);
       
        List<WebElement> select = driver.findElements(By.xpath("//li[@role='option']"));
        System.out.println(DateUtil.getDate()+"-列表的List的长度是："+select.size());
        select.get(0).click();
    }
    public static void clickFindBtn(){
        WebElement e2 = driver.findElement(By.xpath("//div[@id='search-btn']"));
        e2.click();
        System.out.println(DateUtil.getDate()+"-------点击查询弹出验证码图片----------------");
    }





    /**
     * 模拟移动滑块
     * @param driver
     * @param ele
     * @param distance
     */
    public static void move(ChromeDriver driver, WebElement ele, int distance) throws Exception {
        int randomTime = 0;
        if (distance > 90) {
            randomTime = 250;
        } else if (distance > 80 && distance <= 90) {
            randomTime = 150;
        }
        List<Integer> track = getMoveTrack(distance - 2);
        int moveY = 1;

            Actions actions = new Actions(driver);
            actions.clickAndHold(ele).perform();
            Thread.sleep(200);
            for (int i = 0; i < track.size(); i++) {
                actions.moveByOffset(track.get(i), moveY).perform();
                Thread.sleep(new Random().nextInt(300) + randomTime);
            }
            Thread.sleep(200);
            actions.release(ele).perform();

    }

    /**
     * 根据距离获取滑动轨迹
     * @param distance 需要移动的距离
     * @return
     */
    public static List<Integer> getMoveTrack(int distance) {
        List<Integer> track = new ArrayList<>();// 移动轨迹
        Random random = new Random();
        int current = 0;// 已经移动的距离
        int mid = distance * 4 / 5;// 减速阈值
        int a = 0;
        int move = 0;// 每次循环移动的距离
        while (true) {
            a = random.nextInt(10);
            if (current <= mid) {
                move += a;// 不断加速
            } else {
                move -= a;
            }
            if ((current + move) < distance) {
                track.add(move);
            } else {
                track.add(distance - current);
                break;
            }
            current += move;
        }
        return track;
    }
    public static void slideBtn(){
        int times=0;
        while (true) {
            try{
                Thread.sleep(5000);
            }catch (Exception e){
                System.err.println("slideBtn----Thread.sleep5000"+Cons.current_vo);
                continue;
            }
            System.out.println("-------------是否导出数据成功----------"+Cons.allRegSuccess.contains(Cons.current_vo));
            if(Cons.allRegSuccess.contains(Cons.current_vo)){
                break;
            }
            if(times>10){//查10次查不出后重新查
                try {
                    invoke(Cons.current_vo);
                    break;
                } catch (Exception e) {
                    System.err.println(DateUtil.getDate()+"slideBtn重试超过10次报错-----再重试invoke也报错 "+Cons.current_vo);
                }
            }

            try{
                By moveBtn = By.cssSelector("div.slider-mask > div.block");//滑动验证按钮
                waitForLoad(driver, moveBtn);
                WebElement moveElemet = driver.findElement(moveBtn);
                Thread.sleep(3000);//这个等待一定要比</获取验证码图片等待>时间长,否则bigImg和tarImg拿不到值
                if(StringUtils.isBlank(Cons.bigImg) ||StringUtils.isBlank(Cons.tarImg)
                        ||!FileUtil.exist(Cons.bigImg)||!FileUtil.exist(Cons.bigImg)){
                    System.err.println("-----slideBtn报错-本地没图片--- "+Cons.current_vo);
                    invoke(Cons.current_vo);
                    break;
                }
                int distance = OpenCvUtil.getDistance(Cons.bigImg,Cons.tarImg);
                if(distance==0){
                    System.err.println("OpenCvUtil.getDistance报错---- "+Cons.current_vo);
                    invoke(Cons.current_vo);
                    break;
                }
                move(driver, moveElemet, distance);
                System.out.println(DateUtil.getDate()+"-----"+Cons.current_vo+"----正常重试次数---------"+times);
            }catch (Exception e){
                System.err.println("slideBtn报错 "+Cons.current_vo);
                try {
                    System.err.println(DateUtil.getDate()+"----"+Cons.current_vo+"-------slideBtn报错重新开始--------重试次数："+times);
                    invoke(Cons.current_vo);
                    break;
                }catch (Exception ex){
                    System.err.println(DateUtil.getDate()+"slideBtn报错-----重试invoke也报错 "+Cons.current_vo);
                }

            }
            System.out.println(DateUtil.getDate()+"----------"+Cons.current_vo+"---------滑动验证码结果结束---------------------------------");
            times++;
        }
    }
    public static void clickNextPage(int currentPag){
        By pageBtn = By.cssSelector("ul.el-pager > li.number");//滑动验证按钮
        waitForLoad(driver, pageBtn);
        List<WebElement> select = driver.findElements(pageBtn);
        System.out.println(DateUtil.getDate()+"---"+Cons.current_vo+"---clickNextPage——List的长度是："+select.size());
        for (WebElement vo: select) {
            if(currentPag==Integer.parseInt(vo.getText().trim())){
                vo.click();
                System.out.println(DateUtil.getDate()+"-----"+Cons.current_vo+"-----点击翻页按钮：页码是-------"+currentPag);
                break;
            }
        }
    }


}
