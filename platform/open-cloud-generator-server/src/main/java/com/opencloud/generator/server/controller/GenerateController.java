package com.opencloud.generator.server.controller;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.opencloud.common.model.ResultBody;
import com.opencloud.common.utils.DateUtils;
import com.opencloud.common.utils.WebUtils;
import com.opencloud.generator.server.service.GenerateConfig;
import com.opencloud.generator.server.service.GeneratorService;
import com.opencloud.generator.server.utils.ZipUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * @author: liuyadu
 * @date: 2019/7/19 15:26
 * @description:
 */
@Api(tags = "在线代码生成器")
@RestController
@RequestMapping("/generate")
public class GenerateController {
    /**
     * 获取所有表信息
     *
     * @return
     */
    @ApiOperation(value = "获取所有表信息", notes = "获取所有表信息")
    @PostMapping("/tables")
    public ResultBody<List<TableInfo>> tables(
            @RequestParam(value = "type") String type,
            @RequestParam(value = "driverName") String driverName,
            @RequestParam(value = "url") String url,
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password
    ) {
        GlobalConfig gc = new GlobalConfig();
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.getDbType(type));
        dsc.setDriverName(driverName);
        dsc.setUrl(url);
        dsc.setUsername(username);
        dsc.setPassword(password);
        StrategyConfig strategy = new StrategyConfig();
        TemplateConfig templateConfig = new TemplateConfig();
        ConfigBuilder config = new ConfigBuilder(new PackageConfig(), dsc, strategy, templateConfig, gc);
        List<TableInfo> list = config.getTableInfoList();
        return ResultBody.ok().data(list);
    }


    @ApiOperation(value = "代码生成并下载", notes = "代码生成并下载")
    @PostMapping("/download")
    public ResultBody<List<TableInfo>> download(
            @RequestParam(value = "type") String type,
            @RequestParam(value = "driverName") String driverName,
            @RequestParam(value = "url") String url,
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "author") String author,
            @RequestParam(value = "parentPackage") String parentPackage,
            @RequestParam(value = "moduleName") String moduleName,
            @RequestParam(value = "includeTables") String includeTables,
            @RequestParam(value = "tablePrefix") String tablePrefix,
            HttpServletResponse response
    ) throws Exception {
        String outputDir = System.getProperty("user.dir") + File.separator + "temp" + File.separator + DateUtils.getCurrentTimestampStr();
        GenerateConfig config = new GenerateConfig();
        config.setDbType(DbType.getDbType(type));
        config.setJdbcUrl(url);
        config.setJdbcUserName(username);
        config.setJdbcPassword(password);
        config.setJdbcDriver(driverName);
        config.setAuthor(author);
        config.setParentPackage(parentPackage);
        config.setModuleName(moduleName);
        config.setIncludeTables(includeTables.split(","));
        config.setTablePrefix(tablePrefix.split(","));
        config.setOutputDir(outputDir);
        GeneratorService.execute(config);
        String fileName = moduleName + ".zip";
        String filePath = outputDir + File.separator + fileName;
        // 压缩目录
        String[] srcDir = {outputDir};
        ZipUtil.toZip(srcDir, filePath, true);
        download(response, filePath, fileName);
        // 删除目录
        FileUtils.deleteDirectory(new File(outputDir));
        return ResultBody.ok();
    }

    /**
     * 文件下载
     *
     * @param response
     * @param filePath
     * @param fileName
     * @throws IOException
     */
    private void download(HttpServletResponse response, String filePath, String fileName) throws IOException {
        WebUtils.setFileDownloadHeader(response, fileName);
        BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(filePath));
        BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream());
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        outStream.flush();
        inStream.close();
    }
}
