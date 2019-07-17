package com.youkeda.wacai.web.control;

import com.youkeda.wacai.web.model.*;
import com.youkeda.wacai.web.service.FinanceService;
import com.youkeda.wacai.web.service.RecordService;
import com.youkeda.wacai.web.service.impl.JdFinanceServiceImpl;
import com.youkeda.wacai.web.service.impl.RecordServiceImpl;
import com.youkeda.wacai.web.service.impl.YuebaoFinanceServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AccountingController{
    @RequestMapping(path = "/accounting")
    public Accounting accounting(Accounting accounting){
        int result = accounting.getCash()+accounting.getIncome()-accounting.getCharges()-accounting.getRent()-accounting.getEat()-accounting.getTreat()-accounting.getKtv();
        accounting.setBalance(result);
        return accounting;
    }

    private static final List<AccountingRecord> records = new ArrayList<>();

    @RequestMapping(path = "/record")
    public String record(AccountingRecord record){
    if (record.getAmount()==0){
        return "";
    }
    Date time = new Date();
    record.setTime(time);
    records.add(record);
    String result="";
    for (AccountingRecord accountingRecord:records){
        result = result + "金额："+accountingRecord.getAmount()
                +"记录时间"+accountingRecord.getTime()
                +"发生时间"+accountingRecord.getCreatTime()
                +"科目"+accountingRecord.getCategory()
                +"类别"+accountingRecord.getType();
        result= result+"<br/>";
    }
    return result;
    }

    @RequestMapping(path = "/search")
    public String search(@RequestParam("amount")int amount){
        List<AccountingRecord> filter = records.stream().filter(str->str.getAmount()>amount).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        for (AccountingRecord item:filter) {
            sb.append(item.getAmount());
        }
        String csb = sb.toString();
        System.out.println(csb);
        return csb;
    }

    private static final List<Payinfo> payinfos=new ArrayList<>();

    @PostConstruct
    public void init(){
        //初始化白条数据
        Payinfo baitiao = new Payinfo();
        baitiao.setPayType(PayType.baitiao);
        baitiao.setBillingDate(10);
        baitiao.setDueDate(20);
        payinfos.add(baitiao);
        //初始化花呗数据
        Payinfo huabei = new Payinfo();
        huabei.setPayType(PayType.huabei);
        huabei.setBillingDate(20);
        huabei.setDueDate(10);
        payinfos.add(huabei);
        //初始化信用卡数据
        CreditCard creditCard = new CreditCard();
        creditCard.setPayType(PayType.creditcard);
        creditCard.setBillingDate(5);
        creditCard.setDueDate(25);
        creditCard.setName("招商银行信用卡");
        creditCard.setCardNumber("1111111111111");
        payinfos.add(creditCard);
    }

    @RequestMapping(path = "/pay")
    public Payinfo pay(@RequestParam("amount")double amount,@RequestParam("payType") PayType payType,@RequestParam("stagesCount")int stagesCount){
        List<Payinfo> payinfoList = payinfos.stream().filter(payinfo1 -> payinfo1.getPayType().equals(payType)).collect(Collectors.toList());
        if (payType.equals(PayType.creditcard)){
            CreditCard creditCard=(CreditCard)payinfoList.get(0);
            //返回数据
            CreditCard result=new CreditCard();
            result.setBillingDate(creditCard.getBillingDate());
            result.setDueDate(creditCard.getDueDate());
            result.setPayType(payType);
            result.setAmount(amount);
            result.setStagesCount(stagesCount);
            result.setName(creditCard.getCardNumber());
            result.setCardNumber(creditCard.getCardNumber());
            return result;
        }else {
            Payinfo payinfo = payinfoList.get(0);
            //返回数据
            Payinfo result= new Payinfo();
            result.setBillingDate(payinfo.getBillingDate());
            result.setDueDate(payinfo.getDueDate());
            result.setPayType(payType);
            result.setAmount(amount);
            result.setStagesCount(stagesCount);
            return result;
        }
    }

    @RequestMapping(path = "/finance")
    public FinanceInfo financeInfo(@RequestParam("type")FinanceType type,@RequestParam("amount")double amount,@RequestParam("days")int days){
        FinanceService financeService= null;
        //根据不同类型，实例化不同服务实现者.

        if (FinanceType.yuebao.equals(type)){
            financeService = new YuebaoFinanceServiceImpl();
        }else if (FinanceType.jd.equals(type)){
            financeService = new JdFinanceServiceImpl();
        }else{
            return null;
        }
        return financeService.invest(amount,days);
    }

    private static RecordService recordService = new RecordServiceImpl();

    @RequestMapping(path = "/query")
    public List<AccountingRecord> query(){
        return recordService.query();
    }

}
