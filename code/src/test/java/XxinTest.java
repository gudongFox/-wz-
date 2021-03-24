
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.MyPoiUtil;

import com.cmcu.mcc.business.dao.BusinessRecordMapper;
import com.cmcu.mcc.business.service.BusinessContractService;
import com.cmcu.mcc.business.service.BusinessInputContractService;
import com.cmcu.mcc.business.service.BusinessRecordService;
import com.cmcu.mcc.comm.exportDoc.CheckTextRenderPolicy;
import com.cmcu.mcc.comm.exportDoc.ExportUtils;
import com.cmcu.mcc.comm.exportDoc.FlowsTableRenderPolicy;
import com.cmcu.mcc.five.service.FiveOaWordSizeService;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeSysMapper;
import com.cmcu.mcc.hr.dao.HrQualifyMapper;
import com.cmcu.mcc.hr.dto.HrDeptDto;
import com.cmcu.mcc.hr.entity.HrDept;
import com.cmcu.mcc.hr.entity.HrEmployee;
import com.cmcu.mcc.hr.entity.HrEmployeeSys;
import com.cmcu.mcc.hr.entity.HrQualify;
import com.cmcu.mcc.hr.service.*;
import com.cmcu.mcc.oa.service.OaNoticeService;

import com.cmcu.mcc.sys.service.SysRoleService;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.data.style.Style;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.xml.rpc.ServiceException;
import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;



/**
 * @ProjectName: mcc
 * @Package: PACKAGE_NAME
 * @ClassName: XxinTest
 * @Author: Administrator
 * @Description:
 * @Date: 2019/6/17 8:54
 * @Version: 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class XxinTest {
    @Autowired
    BusinessContractService businessContractService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;
    @Autowired
    HrEmployeeSysMapper hrEmployeeSysMapper;
    @Autowired
    HrDeptService hrDeptService;
    @Autowired
    HrPositionService hrPositionService;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    BusinessInputContractService businessInputContractService;
    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    OaNoticeService oaNoticeService;
    @Autowired
    ProcessEngine processEngine;
    @Autowired
    SelectEmployeeService selectEmployeeService;

    @Autowired
    FiveOaWordSizeService fiveOaWordSizeService;
    @Resource
    HrQualifyMapper  hrQualifyMapper;
    @Autowired
    TaskService taskService;
    @Autowired
    BusinessRecordService businessRecordService;
    @Autowired
    BusinessRecordMapper businessRecordMapper;

    @Test
    public void test31() throws IOException, ServiceException {
        //taskService.getVariables()

    }
    //重新生成项目号
    @Test
    public void test22() throws IOException, ServiceException {

        String exchangeLogin1="2399";
        String exchangeLogin2="2192";
        String tempChangeLogin="justtemp";

        //先把luodond的设置给特殊的temp
        for(Task task:taskService.createTaskQuery().taskAssignee(exchangeLogin1).list()){
            taskService.setAssignee(task.getId(),tempChangeLogin);
        }

        //把hnz的设置给luodong
        for(Task task:taskService.createTaskQuery().taskAssignee(exchangeLogin2).list()){
            taskService.setAssignee(task.getId(),exchangeLogin1);
        }

        //把temp的设置给hnz
        for(Task task:taskService.createTaskQuery().taskAssignee(tempChangeLogin).list()){
            taskService.setAssignee(task.getId(),exchangeLogin2);
        }

    }


    /**
     * 根据流程定义 得到流程的图片
     * @return
     */
    public InputStream getCustomProcessInstanceStream(String processDefinitionKey) {
     /*   List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).processDefinitionTenantIdLike("mcc")
                .orderByProcessDefinitionVersion()  //使用流程定义的版本降序排列
                .desc().list();
        BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(list.get(0).getId());
        CustomProcessDiagramGenerator diagramGenerator = (CustomProcessDiagramGenerator)processEngine.getProcessEngineConfiguration().setProcessDiagramGenerator(new CustomProcessDiagramGenerator()).getProcessDiagramGenerator();
        InputStream inputStream = diagramGenerator.buildPngInputStream(bpmnModel,new ArrayList<>());
        return inputStream;*/
     return null;
    }
    /**
     * 根据流程定义 得到第一个节点
     * @return
     */
    public String getFirstUserTaskNameByKey(String processDefinitionKey) {
        List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).processDefinitionTenantIdLike("mcc")
                .orderByProcessDefinitionVersion()  //使用流程定义的版本降序排列
                .desc().list();
        BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(list.get(0).getId());
        FlowElement userTask1 = bpmnModel.getMainProcess().getFlowElement("userTask1");
        return userTask1.getName();
    }
    public  void testWord(String filePath){
        try{
            FileInputStream in = new FileInputStream(filePath);//载入文档 //如果是office2007  docx格式
            if(filePath.toLowerCase().endsWith("docx")){
                //word 2007 图片不会被读取， 表格中的数据会被放在字符串的最后
                XWPFDocument xwpf = new XWPFDocument(in);//得到word文档的信息
//             List<XWPFParagraph> listParagraphs = xwpf.getParagraphs();//得到段落信息
                Iterator<XWPFTable> it = xwpf.getTablesIterator();//得到word中的表格

                while(it.hasNext()){
                    XWPFTable table = it.next();
                    List<XWPFTableRow> rows=table.getRows();
                    //读取每一行数据
                    String fileType = "";
                    for (int i = 2; i < rows.size(); i++) {
                        XWPFTableRow  row = rows.get(i);
                        //读取每一列数据
                        List<XWPFTableCell> cells = row.getTableCells();
                            XWPFTableCell cell1=cells.get(0);
                            if(!Strings.isNullOrEmpty(cell1.getText())) {
                                fileType =cell1.getText();
                            }
                            XWPFTableCell cell2=cells.get(2);
                            //输出当前的单元格的数据

                    }
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
/*    @Test
    public void deleteProject(){
        int constractId =7;

        BusinessContract model = businessContractMapper.selectByPrimaryKey(constractId);

        //删除当前 合同流程
        long count1 = runtimeService.createProcessInstanceQuery().processInstanceId(model.getProcessInstanceId()).count();
        if(count1!=0) {
            runtimeService.deleteProcessInstance(model.getProcessInstanceId(), "admin");
            historyService.deleteHistoricProcessInstance(model.getProcessInstanceId());
        }

        model.setDeleted(true);
        model.setGmtModified(new Date());
        businessContractMapper.updateByPrimaryKey(model);

        //根据合同id 查询项目中的流程
        Map params = Maps.newHashMap();
        params.put("projectId",model.getId());
        //分期
        List<EdProjectStep> edProjectSteps = edProjectStepMapper.selectAll(params);
        for(EdProjectStep edProjectStep:edProjectSteps){
            EdProjectStep item = edProjectStepMapper.selectByPrimaryKey(edProjectStep.getId());
            item.setDeleted(true);
            edProjectStepMapper.updateByPrimaryKey(item);
        }
        //任务书
        List<EdTask> edTasks = edTaskMapper.selectAll(params);
        for(EdTask edTask:edTasks){
            EdTask item = edTaskMapper.selectByPrimaryKey(edTask.getId());
            item.setDeleted(true);
            edTaskMapper.updateByPrimaryKey(item);

            long count = runtimeService.createProcessInstanceQuery().processInstanceId(edTask.getProcessInstanceId()).count();
            if(count!=0) {
                runtimeService.deleteProcessInstance(edTask.getProcessInstanceId(), "admin");
                historyService.deleteHistoricProcessInstance(edTask.getProcessInstanceId());
            }
        }
        //策划书
        List<EdArrange> edArranges = edArrangeMapper.selectAll(params);
        for(EdArrange edArrange:edArranges){
            EdArrange item = edArrangeMapper.selectByPrimaryKey(edArrange.getId());
            item.setDeleted(true);
            edArrangeMapper.updateByPrimaryKey(item);
            long count = runtimeService.createProcessInstanceQuery().processInstanceId(edArrange.getProcessInstanceId()).count();
            if(count!=0) {
                runtimeService.deleteProcessInstance(edArrange.getProcessInstanceId(), "admin");
                historyService.deleteHistoricProcessInstance(edArrange.getProcessInstanceId());
            }
        }
        //验证记录
        List<EdHouseValidate> edHouseValidates = edHouseValidateMapper.selectAll(params);
        for(EdHouseValidate edHouseValidate:edHouseValidates){
            EdHouseValidate item = edHouseValidateMapper.selectByPrimaryKey(edHouseValidate.getId());
            item.setDeleted(true);
            edHouseValidateMapper.updateByPrimaryKey(item);
            long count = runtimeService.createProcessInstanceQuery().processInstanceId(edHouseValidate.getProcessInstanceId()).count();
            if(count!=0) {
                runtimeService.deleteProcessInstance(edHouseValidate.getProcessInstanceId(), "admin");
                historyService.deleteHistoricProcessInstance(edHouseValidate.getProcessInstanceId());
            }
        }
        //设计信息联系单
        List<EdHouseRefer> edHouseRefers = edHouseReferMapper.selectAll(params);
        for(EdHouseRefer edHouseRefer:edHouseRefers){
            EdHouseRefer item = edHouseReferMapper.selectByPrimaryKey(edHouseRefer.getId());
            item.setDeleted(true);
            edHouseReferMapper.updateByPrimaryKey(item);
            long count = runtimeService.createProcessInstanceQuery().processInstanceId(edHouseRefer.getProcessInstanceId()).count();
            if(count!=0) {
                runtimeService.deleteProcessInstance(edHouseRefer.getProcessInstanceId(), "admin");
                historyService.deleteHistoricProcessInstance(edHouseRefer.getProcessInstanceId());
            }
        }
        //审定记录
        List<EdValidateBatch> edValidateBatchs = edValidateBatchMapper.selectAll(params);
        for(EdValidateBatch edValidateBatch:edValidateBatchs){
            EdValidateBatch item = edValidateBatchMapper.selectByPrimaryKey(edValidateBatch.getId());
            item.setDeleted(true);
            edValidateBatchMapper.updateByPrimaryKey(item);
            long count = runtimeService.createProcessInstanceQuery().processInstanceId(edValidateBatch.getProcessInstanceId()).count();
            if(count!=0) {
                runtimeService.deleteProcessInstance(edValidateBatch.getProcessInstanceId(), "admin");
                historyService.deleteHistoricProcessInstance(edValidateBatch.getProcessInstanceId());
            }
        }

    }

    @Test
    public void deleteProcess(){
        String processId ="347507";
        runtimeService.deleteProcessInstance(processId, "admin");
        historyService.deleteHistoricProcessInstance(processId);
    }*/
    @Test
    //已知列头
    public void test() throws IOException {
/*        String classpath = this.getClass().getResource("/").getPath().replaceFirst("/", "");
        String webappRoot = classpath.replaceAll("/test-classes", "");
        String filePath =webappRoot+"mcc/assets/doc/房建-设计与开发任务书.docx";*/
        String filePath = "D:/test/MCC-房建-设计与开发任务书.docx";
        Style textStyle = new Style();
        textStyle.setBold(false);//加粗
        textStyle.setStrike(false);//删除线
        textStyle.setItalic(false);//斜体
        textStyle.setColor("000000");//颜色
        textStyle.setUnderLine(false);//下划线
        textStyle.setFontFamily("微软雅黑");//字体
        textStyle.setFontSize(12);//字号
        textStyle.setHighlightColor(STHighlightColor.Enum.forString("none"));//背景高亮色


        Configure.ConfigureBuilder configureBuilder = Configure.newBuilder().customPolicy("detail_table", new DetailTablePolicy(2, 4, 12));
        //configureBuilder.customPolicy("detail_table2", new DetailTablePolicy(2, 4, 12));
        configureBuilder.addPlugin('%', new FlowsTableRenderPolicy());
        configureBuilder.addPlugin('$', new CheckTextRenderPolicy(1));
        XWPFTemplate template = XWPFTemplate.compile(filePath, configureBuilder.build()).render(new HashMap<String, Object>() {{
            TextRenderData textRenderData = new TextRenderData("00FF00", "253");
            textRenderData.setStyle(textStyle);
            //文本 textRenderData
            put("projectName", textRenderData);
            put("constructionOrg", new TextRenderData("000000", "1111111111111111111111"));

            //checkbox 字典 NumbericRenderData 列表{{*}}
            String text1 = "省市级优秀工程设计";
            String text1Sub = " √ ☑ ✔<br>";
            String text2 = "  合格";
            String text2Sub = " □ ☒ ✘<br>";
            String text3 = "国家级优秀工程设计";
            String text3Sub = "  □ ☒ ✘<br>";
            TextRenderData textRenderData1 = new TextRenderData(text1 + text1Sub + text2 + text2Sub + text3 + text3Sub);
            put("checkbox", textRenderData1);
            //put("hello",new DocxRenderData());
            put("constructionQuality", ExportUtils.getList(Arrays.asList("111111", "22222", "33333"),"a."));

            //选择框 字典sss
           /* List<SysCodeDto> list = sysCodeService.listDataByCatalog("质量要求");
            List<Map> constructionQualitys =new ArrayList<>();
            for(SysCodeDto dto:list){
                Map map=new HashMap();
                map.put("text",dto.getCode());
                if(dto.getCode().equals(item.getConstructionQuality())) {
                    map.put("select", " ✔    ");
                }else{
                    map.put("select", "     ");
                }
                constructionQualitys.add(map);
            }
            String constructionQuality="";
            for(Map map:constructionQualitys){
                constructionQuality=constructionQuality+map.get("text").toString() + map.get("select");
            }
            put("constructionQuality",constructionQuality);*/


            //已知列头 填充数据
            List<String> row0 = Arrays.asList("张三", "研<br>究生", "1", "2");
            List<String> row1 = Arrays.asList("李四", "博<br>士", "1", "2");
            List<String> row2 = Arrays.asList("王五", "博士后", "1", "2");

            put("detail_table", Arrays.asList(row0, row1, row2));
            put("detail_table2", Arrays.asList(row0, row1, row2));

            //新建 表格 流程
         /*   List<ActHistoryDto> nodes = actService.listPassedHistoryDto("5197560");
            List<RowRenderData> rows = new ArrayList<>();
            for (int i = 0; i < nodes.size(); i++) {
                if (i % 2 == 0) {
                    RowRenderData row = RowRenderData.build(nodes.get(i).getActivityName(), nodes.get(i).getUserName() + "  " + nodes.get(i).getEndTime(),
                            nodes.get(i + 1).getActivityName(), nodes.get(i + 1).getUserName() + "  " + nodes.get(i + 1).getEndTime());
                    rows.add(row);
                }
            }
            MiniTableRenderData miniTableRenderData = new MiniTableRenderData(rows);
            //表格宽度 （cm）
            miniTableRenderData.setWidth(18.85f);
            put("flows", miniTableRenderData);
*/
        }});

        String targetPath = "D:/test/out_template.docx";
        FileOutputStream out = new FileOutputStream(targetPath);
        template.write(out);


        out.flush();
        out.close();
        template.close();
    }

    public class DetailTablePolicy extends DynamicTableRenderPolicy {

        // 填充数据所在行数
        int startRow;
        // 填充每行的列数
        int colNum;
        //最小填充数量
        int minNum;

        DetailTablePolicy(int startRow, int colNum, int minNum) {
            this.colNum = colNum;
            this.startRow = startRow;
            this.minNum = minNum;
        }

        @Override
        public void render(XWPFTable table, Object data) {
            if (null == data) return;

            // 填充明细
            List<List<String>> details = (List<List<String>>) data;
            //原表格高度
            int height = table.getRow(startRow - 1).getHeight();
            if (null != details) {
                //数据大于最少显示
                if (details.size() > minNum) {
                    for (int i = 0; i < details.size(); i++) {
                        XWPFTableRow insertNewTableRow = table.insertNewTableRow(startRow + i);
                        //新行高度
                        insertNewTableRow.getCtRow().addNewTrPr().addNewTrHeight().setVal(BigInteger.valueOf(height + 70));
                        for (int j = 0; j < colNum; j++) {
                            XWPFTableCell cell = insertNewTableRow.createCell();
                            XWPFParagraph para = cell.getParagraphs().get(0);//对某个单元格设置段落，

                            para.setAlignment(ParagraphAlignment.LEFT);//需要设置，否则中文换行会很生硬很难看
                           String[] strings = details.get(i).get(j).split("<br>");
                            for (String text : strings) {
                                XWPFRun run = para.createRun();//对某个段落设置格式
                                run.setText(text.trim());
                                run.addBreak();//换行
                            }
                            //cell.setText(details.get(i).get(j));
                            //单元格内容居中
                            CTTc cttc = cell.getCTTc();
                            CTTcPr ctPr = cttc.addNewTcPr();
                            ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);
                            cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
                        }
                    }
                } else {
                    for (int i = 0; i < details.size(); i++) {
                        XWPFTableRow insertNewTableRow = table.insertNewTableRow(startRow + i);
                        //新行高度
                        insertNewTableRow.getCtRow().addNewTrPr().addNewTrHeight().setVal(BigInteger.valueOf(height + 70));
                        for (int j = 0; j < colNum; j++) {
                            XWPFTableCell cell = insertNewTableRow.createCell();
                            cell.setText(details.get(i).get(j));
                            XWPFParagraph para = cell.getParagraphs().get(0);//对某个单元格设置段落，

                            para.setAlignment(ParagraphAlignment.LEFT);//需要设置，否则中文换行会很生硬很难看
                            String[] strings = details.get(i).get(j).split("<br>");
                            for (String text : strings) {
                                XWPFRun run = para.createRun();//对某个段落设置格式
                                run.setText(text.trim());
                                run.addBreak();//换行
                            }
                            //单元格内容居中
                            CTTc cttc = cell.getCTTc();
                            CTTcPr ctPr = cttc.addNewTcPr();
                            ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);
                            cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
                        }
                    }
                    //补充空数据
                    for (int i = 0; i < minNum - details.size(); i++) {
                        XWPFTableRow insertNewTableRow = table.insertNewTableRow(startRow + details.size() + i);
                        //新行高度
                        insertNewTableRow.getCtRow().addNewTrPr().addNewTrHeight().setVal(BigInteger.valueOf(height + 70));
                        for (int j = 0; j < colNum; j++) {
                            XWPFTableCell cell = insertNewTableRow.createCell();
                        }
                    }
                }
            }
        }
    }



    public class DetailTablePolicy2 extends DynamicTableRenderPolicy {

        // 填充数据列数
        int colNum;
        //行头宽度
        List<Integer> rowHeadWidths;

        DetailTablePolicy2(List<Integer> rowHeadWidths) {
            this.rowHeadWidths = rowHeadWidths;
        }

        @Override
        public void render(XWPFTable table, Object data) {
            if (null == data) return;

            // 填充数据
            List<List<String>> details = (List<List<String>>) data;
            colNum = details.get(0).size();

            //设置 填充列 宽度
            int rowHeadWidth = 0;
            for (Integer i : rowHeadWidths) {
                rowHeadWidth = rowHeadWidth + i;
            }
            int cellWidth = (100 - rowHeadWidth) / details.size();

            if (null != details) {
                for (int i = 0; i < details.size(); i++) {
                    for (int j = 0; j < colNum; j++) {
                        XWPFTableCell cell = table.getRow(i).createCell();
                        cell.setText(details.get(i).get(j));
                        cell.setWidthType(TableWidthType.PCT);
                        cell.setWidth(cellWidth + "%");
                        //单元格内容居中
                        CTTc cttc = cell.getCTTc();
                        CTTcPr ctPr = cttc.addNewTcPr();
                        ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);
                        cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
                    }
                }
                //设置行头宽度
                for (int i = 0; i < 2; i++) {
                    XWPFTableCell cell = table.getRow(0).getCell(i);
                    cell.setWidthType(TableWidthType.PCT);
                    cell.setWidth(rowHeadWidths.get(i) + "%");
                }
            }
        }
    }

    public ByteArrayInputStream parse(final OutputStream out) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos = (ByteArrayOutputStream) out;
        final ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
        return swapStream;
    }

    /**
     * EXCL 导入人员信息
     * @throws IOException
     */
    @Test
    public void insertByFile() throws IOException {
        String path="C:/Users/CMCUBIM/Desktop/11.xls";
        List<Map> list = MyPoiUtil.listTableData(new FileInputStream( new File(path)), 1, 0, false);
        int updateNum=0;
        for(int i=1;i<=list.size();i++) {
            int deptId=0;
            HrEmployee mode=new HrEmployee();
            Map map = list.get(i - 1);
            Map params= Maps.newHashMap();
            params.put("userLogin", map.get(0).toString());

            //部门
            if (StringUtils.isNotEmpty(map.get(2).toString())){
            String name=    map.get(2).toString();
                String[] split = name.split("/");
                String deptName=split[split.length-1];

                HrDept hrDept = hrDeptService.selectByName(deptName);
                if (hrDept!=null){
                    deptId=hrDept.getId();
                }else {
                    deptId=2;
                }
            }
            //判断系统是否已经录入改人员 跟新部门
            if (PageHelper.count(()->hrEmployeeSysMapper.selectAll(params))>0){
                HrEmployeeSys hrEmployeeSys = hrEmployeeSysMapper.selectAll(params).get(0);
                if (deptId==hrEmployeeSys.getDeptId()){

                }else {
                    hrEmployeeSysService.updateDeptId(hrEmployeeSys.getUserLogin(),deptId);                }
            }
            if (StringUtils.isNotEmpty( map.get(1).toString())){
                mode.setUserName(map.get(1).toString());
            }
            if (StringUtils.isNotEmpty( map.get(0).toString())){
                mode.setUserLogin(map.get(0).toString());
            }

           hrEmployeeSysService.insert(mode.getUserLogin(),mode.getUserName(),deptId,"正式员工");
            updateNum++;
        }

        System.out.println("更新条数:"+updateNum+",新增:"+(list.size()-updateNum));
    }


   @Test
   public void TestHrQuality(){
       Map<String,Object> params=Maps.newHashMap();
       params.put("deleted",false);
       List<HrDeptDto> hrDeptDtos = hrDeptService.selectAll();
      for (HrDeptDto hrDeptDto:hrDeptDtos){
          if ("职能".equals(hrDeptDto.getDeptType())){
              params.put("deptId",hrDeptDto.getId());
              //设计资格
              List<HrQualify>  existList=hrQualifyMapper.selectAll(params);
              for (HrQualify hrQualify:existList){
                  hrQualify.setProjectCharge(false);
                  hrQualify.setDeptName(hrDeptDto.getName());
                  hrQualify.setProjectExeCharge(false);
                  hrQualify.setMajorCharge(false);
                  hrQualify.setDesign(false);
                  hrQualify.setProofread(false);
                  hrQualify.setAudit(false);
                  hrQualify.setApprove(false);
                  hrQualify.setCompanyApprove(false);
                  hrQualify.setDeleted(false);
                  hrQualify.setGmtModified(new Date());
                  hrQualify.setProChief(false);
                  hrQualify.setChiefDesigner(false);
                  hrQualifyMapper.updateByPrimaryKey(hrQualify);
              }
          }

      }

   }
    @Test
   public void test98(){
       Map<String,Object> map=Maps.newHashMap();
       map.put("deleted",false);
       List<HrEmployeeSys> hrEmployeeSys = hrEmployeeSysMapper.selectAll(map);
          for (HrEmployeeSys sys:hrEmployeeSys){
              sys.setGmtModified(new Date());
              hrEmployeeSysMapper.updateByPrimaryKey(sys);
          }

    }

/*    @Test
    public void  test97(){
      //cpDirService.buildDir(32);
        String spell = HanyupinyinUtil.getFullSpell("黄南洲");
        System.out.println(spell);
        String spell1 = HanyupinyinUtil.getFirstSpell("黄南洲");
        System.out.println(spell1);
    }*/
}

//经营管理
//经营管理
        /*TRUNCATE `db_wuzhou_mqh`.`business_bid_apply`;

        TRUNCATE `db_wuzhou_mqh`.`business_bid_attend`;

        TRUNCATE `db_wuzhou_mqh`.`business_bid_contract`;

        TRUNCATE `db_wuzhou_mqh`.`business_bid_project_charge`;

        TRUNCATE `db_wuzhou_mqh`.`business_contract`;

        TRUNCATE `db_wuzhou_mqh`.`business_contract_detail`;

        TRUNCATE `db_wuzhou_mqh`.`business_customer`;

        TRUNCATE `db_wuzhou_mqh`.`business_input_contract`;

        TRUNCATE `db_wuzhou_mqh`.`business_pre_contract`;

        TRUNCATE `db_wuzhou_mqh`.`business_record`;

        TRUNCATE `db_wuzhou_mqh`.`business_income`;

        TRUNCATE `db_wuzhou_mqh`.`business_subpackage`;

        TRUNCATE `db_wuzhou_mqh`.`business_supplier`;

        TRUNCATE `db_wuzhou_mqh`.`business_supplier_aptitude`;

//cp协同
        TRUNCATE `db_wuzhou_mqh`.`co_app`;

        TRUNCATE `db_wuzhou_mqh`.`cp_dir`;

        TRUNCATE `db_wuzhou_mqh`.`cp_dwg_extraction`;

        TRUNCATE `db_wuzhou_mqh`.`cp_dwg_std`;

        TRUNCATE `db_wuzhou_mqh`.`cp_file`;

        TRUNCATE `db_wuzhou_mqh`.`cp_file_history`;

        TRUNCATE `db_wuzhou_mqh`.`cp_question`;

//ed文件
        TRUNCATE `db_wuzhou_mqh`.`ed_arrange`;

        TRUNCATE `db_wuzhou_mqh`.`ed_arrange_user`;

        TRUNCATE `db_wuzhou_mqh`.`ed_file`;

        TRUNCATE `db_wuzhou_mqh`.`ed_house_archive`;

        TRUNCATE `db_wuzhou_mqh`.`ed_house_change`;

        TRUNCATE `db_wuzhou_mqh`.`ed_house_change_archive`;

        TRUNCATE `db_wuzhou_mqh`.`ed_house_exception`;

        TRUNCATE `db_wuzhou_mqh`.`ed_house_refer`;

        TRUNCATE `db_wuzhou_mqh`.`ed_house_return_visit`;

        TRUNCATE `db_wuzhou_mqh`.`ed_house_return_visit_detail`;

        TRUNCATE `db_wuzhou_mqh`.`ed_house_review`;

        TRUNCATE `db_wuzhou_mqh`.`ed_house_service`;

        TRUNCATE `db_wuzhou_mqh`.`ed_house_validate`;

        TRUNCATE `db_wuzhou_mqh`.`ed_house_validate_mark`;

        TRUNCATE `db_wuzhou_mqh`.`ed_input_ac`;

        TRUNCATE `db_wuzhou_mqh`.`ed_input_approve`;

        TRUNCATE `db_wuzhou_mqh`.`ed_input_de`;

        TRUNCATE `db_wuzhou_mqh`.`ed_input_gs`;

        TRUNCATE `db_wuzhou_mqh`.`ed_input_plan`;

        TRUNCATE `db_wuzhou_mqh`.`ed_input_review`;

        TRUNCATE `db_wuzhou_mqh`.`ed_input_road`;

        TRUNCATE `db_wuzhou_mqh`.`ed_letter`;

        TRUNCATE `db_wuzhou_mqh`.`ed_product`;

        TRUNCATE `db_wuzhou_mqh`.`ed_product_detail`;

        TRUNCATE `db_wuzhou_mqh`.`ed_project`;

        TRUNCATE `db_wuzhou_mqh`.`ed_project_detail`;

        TRUNCATE `db_wuzhou_mqh`.`ed_project_step`;

        TRUNCATE `db_wuzhou_mqh`.`ed_project_tree`;

        TRUNCATE `db_wuzhou_mqh`.`ed_project_tree_state`;

        TRUNCATE `db_wuzhou_mqh`.`ed_sign_paper`;

        TRUNCATE `db_wuzhou_mqh`.`ed_stamp`;

        TRUNCATE `db_wuzhou_mqh`.`ed_step_build`;

        TRUNCATE `db_wuzhou_mqh`.`ed_task`;

        TRUNCATE `db_wuzhou_mqh`.`ed_validate_batch`;

        TRUNCATE `db_wuzhou_mqh`.`ed_validate_mark_location`;

        TRUNCATE `db_wuzhou_mqh`.`ed_validate_mark_remark`;

//explore 

//five.asset 固定资产
        TRUNCATE `db_wuzhou_mqh`.`five_asset_computer`;

//five
        TRUNCATE `db_wuzhou_mqh`.`five.common`;

        TRUNCATE `db_wuzhou_mqh`.`five_act_relevance`;

        TRUNCATE `db_wuzhou_mqh`.`five_content_file`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_arrange`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_arrange_plan`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_arrange_timetable`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_arrange_user`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_company_review`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_countersign`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_design_review`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_design_rule`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_design_sign`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_expert_review`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_house_refer`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_house_validate`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_house_validate_mark`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_out_review`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_plot_apply';

        TRUNCATE `db_wuzhou_mqh`.`five_ed_product`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_product_detail`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_project_review`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_quality_analysis`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_quality_check`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_quality_check_detail`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_quality_issue`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_return_visit`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_return_visit_detail`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_review_apply`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_review_major`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_review_plan`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_review_special`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_service_change`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_service_discuss`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_service_handle`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_special_review`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_stamp`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_task`;

        TRUNCATE `db_wuzhou_mqh`.`five_ed_task_user`;

//five.hr 
        TRUNCATE `db_wuzhou_mqh`.`five_hr_qualify_apply`;

        TRUNCATE `db_wuzhou_mqh`.`five_hr_qualify_apply_chief`;

        TRUNCATE `db_wuzhou_mqh`.`five_hr_qualify_apply_detail`;

        TRUNCATE `db_wuzhou_mqh`.`five_hr_qualify_chief`;

        TRUNCATE `db_wuzhou_mqh`.`five_hr_qualify_external`;

//five.oa
        TRUNCATE `db_wuzhou_mqh`.`five_oa_archival_data`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_association_apply`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_association_change`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_association_payment`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_bid_apply`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_car`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_car_apply`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_computer_application`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_computer_change`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_computer_maintain`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_computer_network`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_computer_network_detail`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_computer_purchase`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_computer_purchase_detail`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_contract_law_examine`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_contract_sign`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_department_post`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_dept_journal`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_dispatch`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_dispatch_academy`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_dispatch_cpc`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_dispatch_cpca_academy`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_employee_dimission_examine`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_employee_entry_examine`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_employee_retire_examine`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_employee_transfer_examine`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_file_instruction`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_file_transfer`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_general_countersign`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_go_abroad`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_go_abroad_train_ask`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_go_abroad_train_ask_detail`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_go_abroad_train_declare`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_information_equipment_apply`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_information_equipment_apply_detail`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_information_equipment_examine`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_information_equipment_procurement`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_information_equipment_procurement_detail`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_institution_count_sign`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_invent_payment`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_invent_payment_detail`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_material_purchase`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_material_purchase_detail`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_message_export`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_newsexamine`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_non_independent_dept_set`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_non_secret_equipment_scrap`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_nonsecret_equipment_scrap_detail`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_out_specialist`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_out_technical_exchange`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_platform_record`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_platform_record_detail`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_pressure_pip_seal_examine`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_process_develop_apply`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_professional_skill_train`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_projectfundplan`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_projectfundplan_detail`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_report`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_review_contract`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_rulelawexamine`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_scientific_task_cost_apply`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_scientific_task_cost_apply_detail`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_sign_quote`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_software`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_software_cost`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_technology_article_examine`;

//hr
        TRUNCATE `db_wuzhou_mqh`.`hr_certification`; //职业资格

        TRUNCATE `db_wuzhou_mqh`.`hr_education`;//员工教育经历

//oa
        TRUNCATE `db_wuzhou_mqh`.`oa_car`

        TRUNCATE `db_wuzhou_mqh`.`oa_car_apply`;

        TRUNCATE `db_wuzhou_mqh`.`oa_knowledge`;

        TRUNCATE `db_wuzhou_mqh`.`oa_notice`;

        TRUNCATE `db_wuzhou_mqh`.`oa_software_apply`;

        TRUNCATE `db_wuzhou_mqh`.`oa_stamp_apply`;

        TRUNCATE `db_wuzhou_mqh`.`oa_technology`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_go_abroad`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_go_abroad_train_ask`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_go_abroad_train_ask_detail`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_go_abroad_train_declare`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_information_equipment_apply`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_information_equipment_apply_detail`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_information_equipment_procurement`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_information_equipment_procurement_detail`;


        TRUNCATE `db_wuzhou_mqh`.`five_oa_decision_making`;

        TRUNCATE `db_wuzhou_mqh`.`five_oa_department_post`;

        TRUNCATE `db_wuzhou_mqh`.`five_finance_back_letter`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_balance`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_company_bank`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_dept_budget`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_dept_budget_detail`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_dept_fund`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_income`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_income_confirm`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_invoice`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_invoice_cancel`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_invoice_collection`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_invoice_detail`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_loan`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_loan_detail`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_node`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_out_supply`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_project_budget`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_project_budget_detail`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_receipt`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_refund`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_refund_detail`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_reimburse`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_reimburse_deduction`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_reimburse_detail`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_self_bank`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_stamp_tax`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_stamp_tax_detail`;

        TRUNCATE `db_wuzhou_mqh`.`five_finance_transfer_accounts`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_transfer_accounts_detail`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_travel_deduction`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_travel_expense`;
        TRUNCATE `db_wuzhou_mqh`.`five_finance_travel_expense_detail`;*/