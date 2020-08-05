package cn.parsexml.controller;

import cn.parsexml.entity.KeyWordCompareDto;
import cn.parsexml.entity.KeyWordCompareResDTO;
import cn.parsexml.entity.Keyword;
import cn.parsexml.service.IKeywordService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/keyword")
public class KeywordController {

    @Resource
    private IKeywordService keywordService;

    private Logger logger = LoggerFactory.getLogger(KeywordController.class);

    @GetMapping("/path")
    public List<Keyword> parseKeyword(String path){
        return keywordService.parseKeywordByPath(path);
    }


    public void exportExcel(List<Keyword> keyWordCompareResDTOList) throws IOException {

        Workbook workbook = new XSSFWorkbook();
        //创建第一个表
        Sheet sheet = workbook.createSheet("pick新旧版本比对信息");
        //记录该创建第几行
        int row = 0;
        for (int j = 0; j < keyWordCompareResDTOList.size(); j++) {
            Keyword keyWordCompareResDTO = keyWordCompareResDTOList.get(j);
            //创建关键字第一行（包括关键字及新pickde的field_value）
            Row row1 = sheet.createRow(row);
            //创建第一个单元格保存关键字name
            Cell cell = row1.createCell(0);
            cell.setCellValue(keyWordCompareResDTO.getKeyword());
            List<String> newWords = keyWordCompareResDTO.getKeywordInfo();
            for (int i = 0; i < newWords.size(); i++) {
                Cell cell1 = row1.createCell(i + 1);
                cell1.setCellValue(newWords.get(i));
            }
            //创建关键字第二行（只包含旧pickde的field_value）
            Row row2 = sheet.createRow(++row);
            //创建第一个单元格(1,1)
            List<String> oldWords = keyWordCompareResDTO.getKeywordInfo();
            if (oldWords != null) {
                for (int i = 0; i < oldWords.size(); i++) {
                    Cell cell2 = row2.createCell(i + 1);
                    cell2.setCellValue(oldWords.get(i));
                }
            }
            row++;
        }
        String name = new SimpleDateFormat("yyyy-MM-dd~HH-mm-ss").format(new Date());
        //后缀名一定要准确 07版xlsx
        String path = "D:\\";
        FileOutputStream fileOutputStream = new FileOutputStream(path + name + ".xlsx");
        workbook.write(fileOutputStream);
        fileOutputStream.close();
    }

    @GetMapping("/getExcel")
    public void getExcel(@RequestParam("oldPick") String oldPick,
                         @RequestParam("newPick") String newPick,
                         HttpServletResponse response, HttpServletRequest request
    ){
        logger.info("输入的第一个参数是"+oldPick);
        logger.info("输入的第二个参数是"+newPick);
        //保存xlsx中数据
        List<KeyWordCompareResDTO> keyWordCompareResDTOList = new ArrayList<KeyWordCompareResDTO>();
        try {
            //获得oldPick的fields和field_value
            List<KeyWordCompareDto> oldWords = keywordService.parseKeywordByPickXml(oldPick);
            //获得newPick的fields和field_value
            List<KeyWordCompareDto> newWords = keywordService.parseKeywordByPickXml(newPick);
            //根据关键字的名称整合oldPick和newPick的field_value

            // 判断解析后的数组对象不为空，为空则xml解析失败
            if (null!=oldWords||null!=newWords){
                if (null !=newWords &&!newWords.isEmpty()) {
                    newWords.forEach(newWord -> {
                        KeyWordCompareResDTO keyWordCompareResDTO = new KeyWordCompareResDTO();
                        keyWordCompareResDTO.setName(newWord.getName());
                        keyWordCompareResDTO.setNewWords(newWord.getWords());
                        if (!oldWords.isEmpty()) {
                            //封装old的words
                            oldWords.forEach(oldWord -> {
                                if (newWord.getName().equals(oldWord.getName())) {
                                    keyWordCompareResDTO.setOldWords(oldWord.getWords());
                                }
                            });
                        }
                        keyWordCompareResDTOList.add(keyWordCompareResDTO);
                    });
                }
                HSSFWorkbook workbook = new HSSFWorkbook();
                //创建第一个表
                Sheet sheet = workbook.createSheet("pick新旧版本比对信息");
                //记录该创建第几行
                int row = 0;
                for (int j = 0; j < keyWordCompareResDTOList.size(); j++) {
                    KeyWordCompareResDTO keyWordCompareResDTO = keyWordCompareResDTOList.get(j);
                    //创建关键字第一行（包括关键字及新pickde的field_value）
                    Row row1 = sheet.createRow(row);
                    //创建第一个单元格保存关键字name
                    Cell cell = row1.createCell(0);
                    cell.setCellValue(keyWordCompareResDTO.getName());
                    List<String> dtoNewWords = keyWordCompareResDTO.getNewWords();
                    for (int i = 0; i < dtoNewWords.size(); i++) {
                        Cell cell1 = row1.createCell(i + 1);
                        cell1.setCellValue(dtoNewWords.get(i));
                    }
                    //创建关键字第二行（只包含旧pickde的field_value）
                    Row row2 = sheet.createRow(++row);
                    //创建第一个单元格(1,1)
                    List<String> dtoOldWords = keyWordCompareResDTO.getOldWords();
                    if (dtoOldWords != null) {
                        for (int i = 0; i < dtoOldWords.size(); i++) {
                            Cell cell2 = row2.createCell(i + 1);
                            cell2.setCellValue(dtoOldWords.get(i));
                        }
                    }
                    row++;
                }
                String name = new SimpleDateFormat("yyyy-MM-dd~HH-mm-ss").format(new Date());
                OutputStream output = response.getOutputStream();
                response.reset();
                response.setContentType("application/vnd.ms-excel;charset=utf-8");
                response.setCharacterEncoding("utf-8");

                //设置浏览器响应头对应的Content-disposition ，这样才会在浏览器中显示下载
                //attachment ：附件
                response.setHeader("Content-disposition", "attachment;filename=" + name + ".xls");
                //wb输出
                workbook.write(output);
                output.close();
                logger.info("文件生成成功");
            }
            else{
                throw new Exception("文件读取失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
