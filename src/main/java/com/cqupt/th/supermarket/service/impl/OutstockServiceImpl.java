package com.cqupt.th.supermarket.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.th.supermarket.entity.Outstock;
import com.cqupt.th.supermarket.service.OutstockService;
import com.cqupt.th.supermarket.mapper.OutstockMapper;
import org.springframework.stereotype.Service;

/**
 * @author 16075
 * @description 针对表【outstock】的数据库操作Service实现
 * @createDate 2023-04-07 12:48:21
 */
@Service("outstockService")
public class OutstockServiceImpl extends ServiceImpl<OutstockMapper, Outstock>
        implements OutstockService {

}



