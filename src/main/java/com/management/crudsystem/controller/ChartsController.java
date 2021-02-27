package com.management.crudsystem.controller;


import com.management.crudsystem.mapper.ChartsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.*;

@Controller
public class ChartsController {
    @Autowired
    private ChartsMapper chartsMapper;

    @GetMapping("/charts")
    public String getCharts(Model model){
        System.out.println("一个请求发送到/charts了！");

        getYouth(model); //把青联图表需要的数据注入到model中
        getSex(model);
        getAge(45,model);
        getPoliticsStatus(model);
        getSector(model);
        getProfession(model);
        getNation(model);

        return "charts";
    }

    @PostMapping("/charts")
    public String getChartsPost(@RequestParam("ageline")String ageline,Model model){
        System.out.println("一个请求发送到/charts了！");

        getYouth(model); //把青联图表需要的数据注入到model中
        getSex(model);
        getAge(Integer.parseInt(ageline),model);
        getPoliticsStatus(model);
        getSector(model);
        getProfession(model);
        getNation(model);

        return "charts";
    }
    
    void getYouth(Model model){
        long Member = 0; //委员
        long standingCommitteeMember = 0; //常委
        long viceChairman = 0; //副主席
        long chairman = 0; //主席

        ArrayList<HashMap<String, Long>> hashMaps = chartsMapper.queryyouthposition();
        System.out.println(hashMaps.toString());
        Iterator<HashMap<String, Long>> mapIterator = hashMaps.iterator();
        while(mapIterator.hasNext()){
            HashMap<String, Long> map = mapIterator.next();
            if("委员".equals(map.get("youthposition"))){
                Member = map.get("count");
                System.out.println("委员 赋值为："+Member);
            }else if("常委".equals(map.get("youthposition"))){
                standingCommitteeMember = map.get("count");
                System.out.println("常委 赋值为："+standingCommitteeMember);
            }else if("副主席".equals(map.get("youthposition"))){
                viceChairman = map.get("count");
                System.out.println("副主席 赋值为："+viceChairman);
            }else if("主席".equals(map.get("youthposition"))){
                chairman = map.get("count");
                System.out.println("主席 赋值为："+chairman);
            }

        }

        model.addAttribute("Member",Member);
        model.addAttribute("standingCommitteeMember",standingCommitteeMember);
        model.addAttribute("viceChairman",viceChairman);
        model.addAttribute("chairman",chairman);

    }


    void getSex(Model model){
        long male = 0; //男
        long female = 0; //女

        ArrayList<HashMap<String, Long>> hashMaps = chartsMapper.querysex();
        System.out.println(hashMaps.toString());
        Iterator<HashMap<String, Long>> mapIterator = hashMaps.iterator();
        while(mapIterator.hasNext()){
            HashMap<String, Long> map = mapIterator.next();
            if("男".equals(map.get("sex"))){
                map.get("sex");
                male = map.get("count");
                System.out.println("男 赋值为："+male);
            }else if("女".equals(map.get("sex"))){
                female = map.get("count");
                System.out.println("女 赋值为："+female);
            }

        }

        model.addAttribute("male",male);
        model.addAttribute("female",female);
    }

    void getAge(int line, Model model){


        int age2029 = 0;//20-29岁的人数
        int age3039 = 0;//30-39岁的人数
        int age4049 = 0;//40-49岁的人数
        int age5059 = 0;//50-59岁的人数
        int age6069 = 0;//60-69岁的人数
        int totalage = 0;//所有人的年龄加起来
        int totalsum = 0;//所有人的人数
        int curyear = Calendar.getInstance().get(Calendar.YEAR);//当前系统年份
        int ageobove = 0;
        int agefollow = 0;

        int ageline = 0; //年龄分界线，用以计算比例
        if(line!=45){
            ageline = line;
        }else{
            ageline = 45;
        }

        ArrayList<HashMap<String, String>> hashMaps = chartsMapper.queryage();
        System.out.println(hashMaps.toString());
        Iterator<HashMap<String, String>> mapIterator = hashMaps.iterator();
        while(mapIterator.hasNext()){
            HashMap<String, String> map = mapIterator.next();
            String birthday = map.get("birthday");
            int birthyear = Integer.parseInt(birthday.substring(0,4));
            int age = curyear - birthyear;

            if(age>=20 && age<=29){
                age2029++;
            }else if(age>=30 && age<=39){
                age3039++;
            }else if(age>=40 && age<=49){
                age4049++;
            }else if(age>=50 && age<=59){
                age5059++;
            }else if(age>=60 && age<=69){
                age6069++;
            }

            if(age>ageline){
                ageobove++;
            }else if(age<=ageline){
                agefollow++;
            }

            totalage+=age;
            totalsum++;

        }

        model.addAttribute("age2029",age2029);
        model.addAttribute("age2029per",String.format("%.1f",age2029*1.0/totalsum*100)+'%');
        model.addAttribute("age3039",age3039);
        model.addAttribute("age3039per",String.format("%.1f",age3039*1.0/totalsum*100)+'%');
        model.addAttribute("age4049",age4049);
        model.addAttribute("age4049per",String.format("%.1f",age4049*1.0/totalsum*100)+'%');
        model.addAttribute("age5059",age5059);
        model.addAttribute("age5059per",String.format("%.1f",age5059*1.0/totalsum*100)+'%');
        model.addAttribute("age6069",age6069);
        model.addAttribute("age6069per",String.format("%.1f",age6069*1.0/totalsum*100)+'%');
        model.addAttribute("ageave",totalsum == 0 ? 0 : totalage/totalsum);
        model.addAttribute("totalsum",totalsum);
        model.addAttribute("ageline",ageline);
        model.addAttribute("ageobove",ageobove);
        model.addAttribute("agefollow",agefollow);

    }

    void getPoliticsStatus(Model model){
        long ccpNumer = 0; //中共党员
        long publicNumber = 0; //群众
        long noneNumber = 0; //无党派
        long mingeNum = 0; //民革
        long minjinNum = 0; //民进

        ArrayList<HashMap<String, Long>> hashMaps = chartsMapper.querypoliticsstatus();
        Iterator<HashMap<String, Long>> hashMapIterator = hashMaps.iterator();
        while(hashMapIterator.hasNext()){
            HashMap<String, Long> hashMap = hashMapIterator.next();
            if("中共".equals(hashMap.get("politicsstatus")) || "中共党员".equals(hashMap.get("politicsstatus"))){
                ccpNumer = hashMap.get("count");
            }else if("群众".equals(hashMap.get("politicsstatus")) || "人民群众".equals(hashMap.get("politicsstatus"))){
                publicNumber = hashMap.get("count");
            }else if("无党派".equals(hashMap.get("politicsstatus")) || "无党派人士".equals(hashMap.get("politicsstatus"))){
                noneNumber = hashMap.get("count");
            }else if("民革".equals(hashMap.get("politicsstatus")) || "民革党员".equals(hashMap.get("politicsstatus")) || "民革党".equals(hashMap.get("politicsstatus"))){
                mingeNum = hashMap.get("count");
            }else if("民进".equals(hashMap.get("politicsstatus")) || "民进党员".equals(hashMap.get("politicsstatus")) || "民进党".equals(hashMap.get("politicsstatus"))){
                minjinNum = hashMap.get("count");
            }
        }

        model.addAttribute("ccpNumer",ccpNumer);
        model.addAttribute("publicNumber",publicNumber);
        model.addAttribute("noneNumber",noneNumber);
        model.addAttribute("mingeNum",mingeNum);
        model.addAttribute("minjinNum",minjinNum);
    }

    void getSector(Model model){
        //data: ["中共党员", "群众", "无党派"]
        StringBuilder sbSectorName = new StringBuilder("");
        //data: [1,2,3]
        StringBuilder sbSectorValue = new StringBuilder("");

        int barTotalSum = 0;


        ArrayList<HashMap<String, Object>> hashMaps = chartsMapper.querysector();
        Iterator<HashMap<String, Object>> iterator = hashMaps.iterator();
        while(iterator.hasNext()){
            HashMap<String, Object> next = iterator.next();
            String sectorName = next.get("sector").toString();
            String sectorCount = next.get("count").toString();
            barTotalSum += Integer.parseInt(sectorCount);
            System.out.println(sectorName+":"+sectorCount);

            sbSectorName.append(sectorName+",");
            sbSectorValue.append(sectorCount+",");
        }

        sbSectorName.setCharAt(sbSectorName.length()-1,' ');
        sbSectorValue.setCharAt(sbSectorValue.length()-1,' ');

        System.out.println("sbSectorName:"+sbSectorName);
        System.out.println("sbSectorValue:"+sbSectorValue);


        model.addAttribute("sbSectorName",sbSectorName.toString());
        model.addAttribute("sbSectorValue",sbSectorValue.toString());
        model.addAttribute("barTotalSum",barTotalSum);

    }

    void getProfession(Model model){
        //data: ["中共党员", "群众", "无党派"]
        StringBuilder sbProfessionName = new StringBuilder("");
        //data: [1,2,3]
        StringBuilder sbProfessionValue = new StringBuilder("");

        int barProfessionTotalSum = 0;


        ArrayList<HashMap<String, Object>> hashMaps = chartsMapper.queryprofession();
        Iterator<HashMap<String, Object>> iterator = hashMaps.iterator();
        while(iterator.hasNext()){
            HashMap<String, Object> next = iterator.next();
            String professionName = next.get("profession").toString();
            String professionCount = next.get("count").toString();
            barProfessionTotalSum += Integer.parseInt(professionCount);
            System.out.println(professionName+":"+professionCount);

            sbProfessionName.append(professionName+",");
            sbProfessionValue.append(professionCount+",");
        }

        sbProfessionName.setCharAt(sbProfessionName.length()-1,' ');
        sbProfessionValue.setCharAt(sbProfessionValue.length()-1,' ');

        System.out.println("sbProfessionName:"+sbProfessionName.toString());
        System.out.println("sbProfessionValue:"+sbProfessionValue.toString());


        model.addAttribute("sbProfessionName",sbProfessionName);
        model.addAttribute("sbProfessionValue",sbProfessionValue);
        model.addAttribute("barProfessionTotalSum",barProfessionTotalSum);

    }

    void getNation(Model model){
        //data: ["中共党员", "群众", "无党派"]
        StringBuilder sbNationName = new StringBuilder("");
        //data: [1,2,3]
        StringBuilder sbNationValue = new StringBuilder("");
        int barNationTotalSum = 0;

        ArrayList<HashMap<String, Object>> hashMaps = chartsMapper.querynation();
        Iterator<HashMap<String, Object>> iterator = hashMaps.iterator();
        while(iterator.hasNext()){
            HashMap<String, Object> next = iterator.next();
            String NationName = next.get("nation").toString();
            String NationCount = next.get("count").toString();
            barNationTotalSum += Integer.parseInt(NationCount);
            System.out.println(NationName+":"+NationCount);

            sbNationName.append(NationName+",");
            sbNationValue.append(NationCount+",");
        }
        sbNationName.setCharAt(sbNationName.length()-1,' ');
        sbNationValue.setCharAt(sbNationValue.length()-1,' ');

        System.out.println("sbNationName:"+sbNationName.toString());
        System.out.println("sbNationValue:"+sbNationValue.toString());

        model.addAttribute("sbNationName",sbNationName);
        model.addAttribute("sbNationValue",sbNationValue);
        model.addAttribute("barNationTotalSum",barNationTotalSum);

    }
}
