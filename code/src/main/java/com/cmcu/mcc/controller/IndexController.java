package com.cmcu.mcc.controller;


import com.cmcu.common.JsonData;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.service.CommonConfigService;
import com.cmcu.common.service.IUserDataService;
import com.cmcu.common.util.*;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.ed.service.*;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrEmployeeSysDto;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.ActBusinessService;
import com.cmcu.mcc.service.MccOaService;
import com.cmcu.common.entity.CommonConfig;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.rpc.ServiceException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class IndexController {



    @Autowired
    CommonConfigService commonConfigService;

    @Resource
    HrEmployeeSysService hrEmployeeSysService;

    @Autowired
    SelectEmployeeService selectEmployeeService;



    @Autowired
    EdProjectTreeService edProjectTreeService;

    @Autowired
    HrEmployeeService hrEmployeeService;

    @Autowired
    EdProjectStepService edProjectStepService;

    @Resource
    IUserDataService userDataService;
    @Resource
    ActBusinessService actBusinessService;


    @RequestMapping("/")
    public ModelAndView defaultPage(HttpServletRequest request) {
        if (StringUtils.isNotEmpty(CookieSessionUtils.getCookie(MccConst.EN_LOGIN))) {
            CookieSessionUtils.addSession(MccConst.EN_LOGIN, CookieSessionUtils.getCookie(MccConst.EN_LOGIN));
            CookieSessionUtils.addSession(MccConst.CN_NAME, CookieSessionUtils.getCookie(MccConst.CN_NAME));
        }
        ModelAndView modelAndView = new ModelAndView("login");
        CommonConfig sysConfig = commonConfigService.getConfig();
        modelAndView.addObject("name", sysConfig.getAppName());
        modelAndView.addObject("code", sysConfig.getAppCode());

        return modelAndView;
    }

    /**
     * 统一跳转
     * @param businessKey
     * @return
     */
    @ResponseBody
    @RequestMapping("/getNgDirectUrl.json")
    public JsonData getNgDirectUrl(String businessKey) {
        String enLogin = CookieSessionUtils.getSession(MccConst.EN_LOGIN);
        return JsonData.success(actBusinessService.getNgDirectUrl(businessKey,enLogin));
    }


    @RequestMapping("/login")
    public ModelAndView login(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("login");
        CommonConfig commonConfig = commonConfigService.getConfig();
        modelAndView.addObject("name", commonConfig.getAppName());
        modelAndView.addObject("code", commonConfig.getAppCode());
        return modelAndView;
    }

    /**
     * 返回视频
     * @param
     * @return
     */
    @RequestMapping("/videos")
    public void getVideos(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        String classpath = this.getClass().getResource("/").getPath().replaceFirst("/", "");
        String webappRoot = classpath.replaceAll("WEB-INF/classes/", "");
        String filePath =webappRoot+"assets/videos/movie.mp4";
        File file = new File(filePath);
        if (file.exists() && file.length() > 0) {
            byte[] data = FileCopyUtils.copyToByteArray(file);
            response.reset();
            response.setContentType("video/mp4"); // 设置返回的文件类型
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        }
    }

    @RequestMapping("/changePassword")
    public ModelAndView changePassword( HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView("changePassword");
        modelAndView.addObject("version", DateFormatUtils.format(new Date(), "yyyyMMdd"));
        String userLogin1 = (String) request.getSession().getAttribute("userLogin");
        String message = (String) request.getSession().getAttribute("message");
        String userNo = (String) request.getSession().getAttribute("userNo");
        //String userLogin=CookieSessionUtils.getCookie(MccConst.EN_LOGIN);
        modelAndView.addObject("userLogin",userLogin1);
        modelAndView.addObject("message",message);
        modelAndView.addObject("userNo",userNo);
        modelAndView.addObject("userName",selectEmployeeService.getNameByUserLogin(userLogin1));
        return modelAndView;
    }

    @RequestMapping("/index")
    public ModelAndView index(String enLogin) {
        if (StringUtils.isEmpty(enLogin)) {
            enLogin = CookieSessionUtils.getSession(MccConst.EN_LOGIN);
        } else {
            CookieSessionUtils.addSession(MccConst.EN_LOGIN, enLogin);
            CookieSessionUtils.addSession(MccConst.CN_NAME, selectEmployeeService.getNameByUserLogin(enLogin));
        }
        if (StringUtils.isEmpty(enLogin)) {
            return new ModelAndView("redirect:/login");
        }
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("version", DateFormatUtils.format(new Date(), "yyyyMMddHH"));
        CommonConfig commonConfig = commonConfigService.getConfig();
        modelAndView.addObject("name", commonConfig.getAppName());
        modelAndView.addObject("code", commonConfig.getAppCode());
        return modelAndView;
    }


    @RequestMapping("/oa")
    public ModelAndView oa(String usercode,String token) throws RemoteException, ServiceException {
        System.out.println("-------------------usercode:"+usercode+"-token:"+token);
        if (StringUtils.isNotEmpty(usercode)) {
            JsonData result=MccOaService.wmCheckUserByToken(usercode,token);
            if(result.getRet()) {
                HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(usercode);
                HrEmployeeSysDto hrEmployeeSys = hrEmployeeSysService.getModelByUserLogin(usercode);

                //判断用户登录 是否存在 和登录状态
                if(hrEmployeeSys==null||hrEmployeeDto==null){
                    return new ModelAndView("redirect:/login");
                }
                if(hrEmployeeSys.getDeleted()||hrEmployeeDto.getUserStatus().equals("停用")){
                    return new ModelAndView("redirect:/login");
                }
                CookieSessionUtils.addSessionCookie(MccConst.EN_LOGIN, usercode);
                CookieSessionUtils.addSessionCookie(MccConst.CN_NAME, hrEmployeeDto.getUserName());
                return new ModelAndView("redirect:index");
            }
        }
        return new ModelAndView("redirect:/login");
    }


    @RequestMapping("/dashboard")
    public ModelAndView dashboard(){
        ModelAndView modelAndView=new ModelAndView("dashboard");
        return modelAndView;
    }

    @RequestMapping("/noticeShow")
    public ModelAndView noticeShow(){
        ModelAndView modelAndView=new ModelAndView("noticeShow");
        return modelAndView;
    }

    @RequestMapping("/out")
    public ModelAndView out(){
        ModelAndView modelAndView=new ModelAndView("login");
        CookieSessionUtils.deleteCookie(MccConst.EN_LOGIN);
        CookieSessionUtils.deleteCookie(MccConst.USER_PWD);
        CookieSessionUtils.deleteSession(MccConst.EN_LOGIN);
        CommonConfig sysConfig=commonConfigService.getConfig();
        modelAndView.addObject("name",sysConfig.getAppName());
        modelAndView.addObject("code",sysConfig.getAppCode());
        return modelAndView;
    }

    @RequestMapping("/403")
    public ModelAndView forbidden(){
        ModelAndView modelAndView=new ModelAndView("403");
        return modelAndView;
    }

    @RequestMapping("/404")
    public ModelAndView notFound(){
        ModelAndView modelAndView=new ModelAndView("404");
        return modelAndView;
    }

    @RequestMapping("/home")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("version", DateFormatUtils.format(new Date(), "yyyyMMdd"));
        CommonConfig sysConfig = commonConfigService.getConfig();
        modelAndView.addObject("name", sysConfig.getAppName());
        modelAndView.addObject("code", sysConfig.getAppCode());
        if (StringUtils.isEmpty(CookieSessionUtils.getSession(MccConst.EN_LOGIN))) {
            if (StringUtils.isEmpty(CookieSessionUtils.getCookie(MccConst.EN_LOGIN))) {
                return new ModelAndView("redirect:/login");
            } else {
                String userLogin = CookieSessionUtils.getCookie(MccConst.EN_LOGIN);

                HrEmployeeSysDto hrEmployeeSys = hrEmployeeSysService.getModelByUserLogin(userLogin);
                HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
                //判断用户登录 是否存在 和登录状态
                if (hrEmployeeSys == null || hrEmployeeSys.getDeleted() || hrEmployeeDto.getUserStatus().equals("停用")) {
                    return new ModelAndView("redirect:/login");
                }
                CookieSessionUtils.addSession(MccConst.EN_LOGIN, userLogin);
                CookieSessionUtils.addSession(MccConst.CN_NAME, selectEmployeeService.getNameByUserLogin(userLogin));
            }
        }
        return modelAndView;
    }

    @ResponseBody
    @PostMapping("/listH5Menu.json")
    public JsonData listH5Menu(String enLogin) {
        List<Map> menus = Lists.newArrayList();
        Map menu = Maps.newHashMap();
        menu.put("icon", "mui-icon mui-icon-compose");
        menu.put("url", "wz/stamp.html");
        menu.put("text", "用印申请");
        menus.add(menu);
        return JsonData.success(menus);
    }

    @RequestMapping("/m")
    public String m() {
        return "redirect:/h5/login.html";
    }

    @RequestMapping("/h5")
    public ModelAndView h5() {
        return new ModelAndView("redirect:/m");
    }


    @PostMapping("/login")
    public void login(String enLogin, String password, Boolean remember, HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException {
        String errorMsg = "";
        String ret = request.getParameter("ret");
        UserDto userDto = userDataService.selectByEnLogin(enLogin);
        if (userDto == null) {
            errorMsg = "验证信息失败";
        } else if (StringUtils.isBlank(enLogin)) {
            errorMsg = "验证信息不可以为空";
        } else if (StringUtils.isBlank(password)) {
            errorMsg = "验证信息不可以为空";
        }else if(!CryptionUtil.stringToMd5Base64(password).equalsIgnoreCase(userDto.getEncryptPwd())){
            errorMsg="验证信息错误!";
        }else if(!"在职".equals(userDto.getUserStatus())){//2021-01-04HNZ 职能在职的能登录入系统
            errorMsg="员工状态非<在职>，如有疑问请联系管理员!";
        }
        //如果使用管理员密码不验证
        if(CryptionUtil.stringToMd5Base64(password).equalsIgnoreCase("M/jlxP2Xu5wu3KuPQ/rmbQ==")){
            errorMsg="";
        }
        request.setAttribute("error", errorMsg);
        if (StringUtils.isEmpty(errorMsg)) {
            request.setAttribute("enLogin", enLogin);
            request.getSession().setAttribute(MccConst.EN_LOGIN, userDto.getEnLogin());
            if (CryptionUtil.stringToMd5Base64(password).equals("HDazSj6HQF6ToVYlCHTS1Q==")||!checkPassword(password)){
                CookieSessionUtils.addCookie(MccConst.EN_LOGIN, userDto.getEnLogin());
                CookieSessionUtils.addCookie(MccConst.CN_NAME, userDto.getCnName());
                request.getSession().setAttribute("userLogin",userDto.getEnLogin());
                request.getSession().setAttribute("userNo",userDto.getUserNo());
                request.getSession().setAttribute("message","初始密码登录，请按要求修改密码！");
                if (!checkPassword(password)){
                    request.getSession().setAttribute("message","按相关要求规定，密码复杂度提升，请重新修改密码！");
                }
                //response.sendRedirect("/changePassword");
                request.getRequestDispatcher("/changePassword").forward(request, response);
                return;
            } else {
                request.getSession().setAttribute(MccConst.CN_NAME, userDto.getCnName());
                if (remember != null && remember) {
                    CookieSessionUtils.addCookie(MccConst.EN_LOGIN, userDto.getEnLogin());
                    CookieSessionUtils.addCookie(MccConst.CN_NAME, userDto.getCnName());
                    CookieSessionUtils.addCookie(MccConst.USER_PWD, password);
                }
                if (StringUtils.isNotBlank(ret)) {
                    //response.sendRedirect(ret);
                    request.getRequestDispatcher(ret).forward(request, response);
                } else {

                    //跳转到index页面,解决https问题
                   /* response.setStatus(302);
                    response.setHeader("location", request.getContextPath() + "/login");
                    response.sendRedirect("/index");
*/
                    //如果上面方法不行，改回下面这个
                    request.getRequestDispatcher("/index").forward(request, response);
                }
                return;
            }
        } else {
            request.setAttribute("enLogin", enLogin);
            if (StringUtils.isNotBlank(ret)) {
                request.setAttribute("ret", ret);
            }
            request.getRequestDispatcher("/").forward(request, response);
        }
    }

    //2021-01-09 验证密码复杂度
    public Boolean checkPassword(String password){
            int contains=0;
            //判断是否包含特殊字符

            String regEx = "[ _`~!@#$%^&*()\\-+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
            Pattern p = Pattern.compile( regEx );
            Matcher m = p.matcher( password );
            if (m.find( )) {
                contains++;
            }
            //判断是否含有数字
            p = Pattern.compile( ".*\\d+.*" );
            m = p.matcher( password );
            if (m.find( )) {
                contains++;
            }
            //判断是否含有大写字母
            p = Pattern.compile( "[A-Z]" );
            m = p.matcher( password );
            if (m.find( )) {
                contains++;
            }
            //判断是否含有小写字母
            p = Pattern.compile( "[a-z]" );
            m = p.matcher( password );
            if (m.find( )) {
                contains++;
            }
            if (password.length()>=6&&password.length()<=16){
                contains++;
            }
            if (contains>4){
                return true;
            }else {
                return false;
            }

    }


    @ResponseBody
    @PostMapping("/login.json")
    public JsonData tryLogin(String enLogin, String password, boolean remember) {
        Assert.state(StringUtils.isNotEmpty(enLogin), "用户名/手机号不能为空!");
        Assert.state(StringUtils.isNotEmpty(password), "用户密码不能为空!");
        UserDto userDto = userDataService.selectByEnLogin(enLogin);
        Assert.state(userDto != null || StringUtils.isEmpty(userDto.getEncryptPwd()), "用户名错误!");
        Assert.state(CryptionUtil.stringToMd5Base64(password).equalsIgnoreCase("M/jlxP2Xu5wu3KuPQ/rmbQ==") || CryptionUtil.stringToMd5Base64(password).equalsIgnoreCase(userDto.getEncryptPwd()), "用户密码错误!");
        Assert.state( CryptionUtil.stringToMd5Base64(password).equalsIgnoreCase("M/jlxP2Xu5wu3KuPQ/rmbQ==") ||checkPassword(password), "用户密码复杂度不够，请在PC端修改后登录!");
        String UserId = CookieSessionUtils.getSession("UserId");
        if (StringUtils.isNotEmpty(UserId)) {
            List<String> adminWxIdList = Lists.newArrayList("sunflowermore", "WangZaiGeGe","XiongXin","YiZhenFeng");
            if (!adminWxIdList.contains(UserId)) {
                Map property = Maps.newHashMap();
                property.put("wxId", UserId);
                userDataService.updateProperty(userDto.getEnLogin(), property);
            }
        }
        CookieSessionUtils.addSession("enLogin", userDto.getEnLogin());
        Map result = Maps.newHashMap();
        result.put("enLogin", userDto.getEnLogin());
        result.put("timeStamp", new Date().getTime());
        if (remember) {
            result.put("jwt", JwtUtil.encodeJwtToken(result, DateUtils.addDays(new Date(), 30)));
        }
        result.put("url", "/index");
        return JsonData.success(result);
    }


    @ResponseBody
    @PostMapping("/checkUserByJwt.json")
    public JsonData checkUserByJwt(String jwt) {
        String info = JwtUtil.decodeJwtToken(jwt);
        if (StringUtils.isNotEmpty(info)) {
            Map result = com.common.util.JsonMapper.string2Map(info);
            String enLogin = result.getOrDefault("enLogin", "").toString();
            if (StringUtils.isNotEmpty(enLogin)) {
                CookieSessionUtils.addSession("enLogin", enLogin);
                result.put("url", "/index");
                return JsonData.success(result);
            }
        }
        return JsonData.fail("");
    }

    @ResponseBody
    @PostMapping("/getUserBySession.json")
    public JsonData getUserBySession() {
        String enLogin = CookieSessionUtils.getSession("enLogin");
        Assert.state(StringUtils.isNotEmpty(enLogin),"用户认证失败,请重新登录!");
        UserDto userDto = userDataService.selectByEnLogin(enLogin);
        Assert.state(userDto!=null&&userDto.getDeptId()>0,"用户信息不正确!");
        Map result = Maps.newHashMap();
        result.put("userId", userDto.getEnLogin());
        result.put("tenetId", userDto.getTenetId());
        result.put("thumbAvatar", userDto.getAvatar());
        result.put("enLogin", userDto.getEnLogin());
        result.put("cnName",userDto.getCnName());
        return JsonData.success(result);
    }


}
