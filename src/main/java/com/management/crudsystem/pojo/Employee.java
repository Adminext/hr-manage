package com.management.crudsystem.pojo;

public class Employee {
    private int userid;
    private String name;
    private String sex;
    private String nation;
    private String politicsstatus;
    private String birthday;
    private String nativeplace;
    private String idnumber;
    private String accademiccareer;
    private String accademicdegree;
    private String stationandposition;
    private String profession;
    private String title;
    private String jobgrade;
    private String majorsocialposts;
    private String importantawardsandhonors;
    private String telephone;
    private String sector;
    private String youthposition;
    private String nominatedunit;
    private String ssstimeandjob;
    private String sssccpjob;
    private String ssscomments;

    public Employee() {
    }

    public Employee(int userid, String name, String sex, String nation, String politicsstatus, String birthday, String nativeplace, String idnumber, String accademiccareer, String accademicdegree, String stationandposition, String profession, String title, String jobgrade, String majorsocialposts, String importantawardsandhonors, String telephone, String sector, String youthposition, String nominatedunit, String ssstimeandjob, String sssccpjob, String ssscomments) {
        this.userid = userid;
        this.name = name;
        this.sex = sex;
        this.nation = nation;
        this.politicsstatus = politicsstatus;
        this.birthday = birthday;
        this.nativeplace = nativeplace;
        this.idnumber = idnumber;
        this.accademiccareer = accademiccareer;
        this.accademicdegree = accademicdegree;
        this.stationandposition = stationandposition;
        this.profession = profession;
        this.title = title;
        this.jobgrade = jobgrade;
        this.majorsocialposts = majorsocialposts;
        this.importantawardsandhonors = importantawardsandhonors;
        this.telephone = telephone;
        this.sector = sector;
        this.youthposition = youthposition;
        this.nominatedunit = nominatedunit;
        this.ssstimeandjob = ssstimeandjob;
        this.sssccpjob = sssccpjob;
        this.ssscomments = ssscomments;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getPoliticsstatus() {
        return politicsstatus;
    }

    public void setPoliticsstatus(String politicsstatus) {
        this.politicsstatus = politicsstatus;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNativeplace() {
        return nativeplace;
    }

    public void setNativeplace(String nativeplace) {
        this.nativeplace = nativeplace;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getAccademiccareer() {
        return accademiccareer;
    }

    public void setAccademiccareer(String accademiccareer) {
        this.accademiccareer = accademiccareer;
    }

    public String getAccademicdegree() {
        return accademicdegree;
    }

    public void setAccademicdegree(String accademicdegree) {
        this.accademicdegree = accademicdegree;
    }

    public String getStationandposition() {
        return stationandposition;
    }

    public void setStationandposition(String stationandposition) {
        this.stationandposition = stationandposition;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJobgrade() {
        return jobgrade;
    }

    public void setJobgrade(String jobgrade) {
        this.jobgrade = jobgrade;
    }

    public String getMajorsocialposts() {
        return majorsocialposts;
    }

    public void setMajorsocialposts(String majorsocialposts) {
        this.majorsocialposts = majorsocialposts;
    }

    public String getImportantawardsandhonors() {
        return importantawardsandhonors;
    }

    public void setImportantawardsandhonors(String importantawardsandhonors) {
        this.importantawardsandhonors = importantawardsandhonors;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getYouthposition() {
        return youthposition;
    }

    public void setYouthposition(String youthposition) {
        this.youthposition = youthposition;
    }

    public String getNominatedunit() {
        return nominatedunit;
    }

    public void setNominatedunit(String nominatedunit) {
        this.nominatedunit = nominatedunit;
    }

    public String getSsstimeandjob() {
        return ssstimeandjob;
    }

    public void setSsstimeandjob(String ssstimeandjob) {
        this.ssstimeandjob = ssstimeandjob;
    }

    public String getSssccpjob() {
        return sssccpjob;
    }

    public void setSssccpjob(String sssccpjob) {
        this.sssccpjob = sssccpjob;
    }

    public String getSsscomments() {
        return ssscomments;
    }

    public void setSsscomments(String ssscomments) {
        this.ssscomments = ssscomments;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "userid=" + userid +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", nation='" + nation + '\'' +
                ", politicsstatus='" + politicsstatus + '\'' +
                ", birthday='" + birthday + '\'' +
                ", nativeplace='" + nativeplace + '\'' +
                ", idnumber='" + idnumber + '\'' +
                ", accademiccareer='" + accademiccareer + '\'' +
                ", accademicdegree='" + accademicdegree + '\'' +
                ", stationandposition='" + stationandposition + '\'' +
                ", profession='" + profession + '\'' +
                ", title='" + title + '\'' +
                ", jobgrade='" + jobgrade + '\'' +
                ", majorsocialposts='" + majorsocialposts + '\'' +
                ", importantawardsandhonors='" + importantawardsandhonors + '\'' +
                ", telephone='" + telephone + '\'' +
                ", sector='" + sector + '\'' +
                ", youthposition='" + youthposition + '\'' +
                ", nominatedunit='" + nominatedunit + '\'' +
                ", ssstimeandjob='" + ssstimeandjob + '\'' +
                ", sssccpjob='" + sssccpjob + '\'' +
                ", ssscomments='" + ssscomments + '\'' +
                '}';
    }
}
