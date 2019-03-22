package com.github.lyd.gateway.provider.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;

@Slf4j
public class IpRegionUtils {
    public static String getRegion(String ip) {

        try {
            //db

            String dbPath = IpRegionUtils.class.getClassLoader().getResource( "data/ip2region.db").getPath();
            File file = ResourceUtils.getFile(dbPath);
            if (!file.exists()) {
                String tmpDir = System.getProperties().getProperty("java.io.tmpdir");
                dbPath = tmpDir + "ip2region.db";
                File tmpFile = new File(dbPath);
                InputStream inputStream = IpRegionUtils.class.getClassLoader().getResourceAsStream("data/ip2region.db");
                FileUtils.copyInputStreamToFile(inputStream, tmpFile);
            }

            //查询算法
            int algorithm = DbSearcher.BTREE_ALGORITHM; //B-tree
            //DbSearcher.BINARY_ALGORITHM //Binary
            //DbSearcher.MEMORY_ALGORITYM //Memory
            try {
                DbConfig config = new DbConfig();
                DbSearcher searcher = new DbSearcher(config, dbPath);

                //define the method
                Method method = null;
                switch (algorithm) {
                    case DbSearcher.BTREE_ALGORITHM:
                        method = searcher.getClass().getMethod("btreeSearch", String.class);
                        break;
                    case DbSearcher.BINARY_ALGORITHM:
                        method = searcher.getClass().getMethod("binarySearch", String.class);
                        break;
                    case DbSearcher.MEMORY_ALGORITYM:
                        method = searcher.getClass().getMethod("memorySearch", String.class);
                        break;
                }

                DataBlock dataBlock = null;
                if (Util.isIpAddress(ip) == false) {
                   log.error("Error: Invalid ip address");
                }

                dataBlock = (DataBlock) method.invoke(searcher, ip);

                return dataBlock.getRegion();

            } catch (Exception e) {
                log.error("Error:{}",e);
            }
        } catch (Exception e) {
            log.error("Error:{}",e);
        }
        return null;
    }

}
