package com.cmcu.mcc.entity.hr;

import java.util.Date;

public class HrIncome {
    private Integer id;

    private String userLogin;

    private String userName;

    private String userIdcard;

    private String workYearNumber;

    private String requestIncomeYear;

    private String workPosition;

    private String yearIncome;

    private String monthIncome;

    private String companyName;

    private String companyAddress;

    private String companyTel;

    private String companyLinkMan;

    private Boolean deleted;

    private Date gmtCreate;

    private Date gmtModified;

    private String processInstanceId;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin == null ? null : userLogin.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserIdcard() {
        return userIdcard;
    }

    public void setUserIdcard(String userIdcard) {
        this.userIdcard = userIdcard == null ? null : userIdcard.trim();
    }

    public String getWorkYearNumber() {
        return workYearNumber;
    }

    public void setWorkYearNumber(String workYearNumber) {
        this.workYearNumber = workYearNumber == null ? null : workYearNumber.trim();
    }

    public String getRequestIncomeYear() {
        return requestIncomeYear;
    }

    public void setRequestIncomeYear(String requestIncomeYear) {
        this.requestIncomeYear = requestIncomeYear == null ? null : requestIncomeYear.trim();
    }

    public String getWorkPosition() {
        return workPosition;
    }

    public void setWorkPosition(String workPosition) {
        this.workPosition = workPosition == null ? null : workPosition.trim();
    }

    public String getYearIncome() {
        return yearIncome;
    }

    public void setYearIncome(String yearIncome) {
        this.yearIncome = yearIncome == null ? null : yearIncome.trim();
    }

    public String getMonthIncome() {
        return monthIncome;
    }

    public void setMonthIncome(String monthIncome) {
        this.monthIncome = monthIncome == null ? null : monthIncome.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress == null ? null : companyAddress.trim();
    }

    public String getCompanyTel() {
        return companyTel;
    }

    public void setCompanyTel(String companyTel) {
        this.companyTel = companyTel == null ? null : companyTel.trim();
    }

    public String getCompanyLinkMan() {
        return companyLinkMan;
    }

    public void setCompanyLinkMan(String companyLinkMan) {
        this.companyLinkMan = companyLinkMan == null ? null : companyLinkMan.trim();
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId == null ? null : processInstanceId.trim();
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}