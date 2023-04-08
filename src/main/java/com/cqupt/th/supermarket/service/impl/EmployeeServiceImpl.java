package com.cqupt.th.supermarket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.th.supermarket.entity.*;
import com.cqupt.th.supermarket.mapper.PositionMapper;
import com.cqupt.th.supermarket.mapper.StoreMapper;
import com.cqupt.th.supermarket.mapper.WarehouseMapper;
import com.cqupt.th.supermarket.query.EmployeeQuery;
import com.cqupt.th.supermarket.service.EmployeeService;
import com.cqupt.th.supermarket.mapper.EmployeeMapper;
import com.cqupt.th.supermarket.service.RegionService;
import com.cqupt.th.supermarket.utils.CommonResult;
import com.cqupt.th.supermarket.vo.EmployeeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 16075
 * @description 针对表【employee(员工表)】的数据库操作Service实现
 * @createDate 2023-03-31 16:17:01
 */
@Service("employeeService")
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
        implements EmployeeService {
    @Resource
    private StoreMapper storeMapper;
    @Resource
    private PositionMapper positionMapper;
    @Autowired
    @Qualifier("regionService")
    private RegionService regionService;
    @Resource
    private WarehouseMapper warehouseMapper;

    @Override
    public CommonResult getEmployeeListPageByPositionId(Integer currentPage, Integer pageSize, Integer positionId, EmployeeQuery employeeQuery) {
        if (currentPage == null || pageSize == null || positionId == null) {
            return CommonResult.error().message("参数错误");
        }
        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();
        employeeQueryWrapper.eq("position_id", positionId);
        employeeQueryWrapper.orderByDesc("gmt_modified");
        if (employeeQuery != null) {
            if (employeeQuery.getName() != null) {
                employeeQueryWrapper.like("name", employeeQuery.getName());
            }
        }
        Page<Employee> employeePage = new Page<>();
        baseMapper.selectPage(employeePage, employeeQueryWrapper);
        long total = employeePage.getTotal();
        List<Employee> records = employeePage.getRecords();
        List<EmployeeVo> rows = getEmployeeVoList(records);
        return CommonResult.ok().data("total", total).data("rows", rows);

    }

    @Override
    public CommonResult getManagerList() {

        QueryWrapper<Position> positionQueryWrapper = new QueryWrapper<>();
        positionQueryWrapper.eq("name", "超市经理");
        Position one = positionMapper.selectOne(positionQueryWrapper);
        if (one == null) {
            return CommonResult.error().message("超市经理职位不存在");
        }
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<Employee>().eq("position_id", one.getId()).eq("warehouse_id", 0);
        List<Employee> managerList = baseMapper.selectList(queryWrapper);
        return CommonResult.ok().data("items", managerList);
    }

    @Override
    public CommonResult getWarehouseManagerList() {
        QueryWrapper<Position> positionQueryWrapper = new QueryWrapper<>();
        positionQueryWrapper.eq("name", "仓库经理");
        Position one = positionMapper.selectOne(positionQueryWrapper);
        if (one == null) {
            return CommonResult.error().message("仓库经理职位不存在");
        }
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<Employee>().eq("position_id", one.getId()).eq("store_id", 0);
        List<Employee> managerList = baseMapper.selectList(queryWrapper);
        return CommonResult.ok().data("items", managerList);
    }

    @Override
    public CommonResult getEmployeeListPage(Integer currentPage, Integer size, EmployeeQuery employeeQuery) {
        if (currentPage == null || size == null) {
            return CommonResult.error().message("参数错误");
        }
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_modified");
        if (employeeQuery != null) {
            if (StringUtils.hasText(employeeQuery.getName())) {
                queryWrapper.like("name", employeeQuery.getName());
            }
            if (employeeQuery.getStoreId() != null) {
                queryWrapper.eq("store_id", employeeQuery.getStoreId());
            }
            if (employeeQuery.getWarehouseId() != null) {
                queryWrapper.eq("warehouse_id", employeeQuery.getWarehouseId());
            }
            if (employeeQuery.getPositionId() != null) {
                queryWrapper.eq("position_id", employeeQuery.getPositionId());
            }
            if (employeeQuery.getStatus() != null) {
                queryWrapper.eq("status", employeeQuery.getStatus());
            }
            if (employeeQuery.getStartTime() != null) {
                queryWrapper.ge("gmt_create", employeeQuery.getStartTime());
            }
            if (employeeQuery.getEndTime() != null) {
                queryWrapper.le("gmt_create", employeeQuery.getEndTime());
            }
        }
        Page<Employee> employeePage = new Page<>(currentPage, size);
        baseMapper.selectPage(employeePage, queryWrapper);
        long total = employeePage.getTotal();
        List<Employee> records = employeePage.getRecords();
        List<EmployeeVo> rows = getEmployeeVoList(records);
        return CommonResult.ok().data("total", total).data("rows", rows);
    }

    @Override
    public CommonResult deleteBatchEmployee(Integer[] ids) {
        if (ids == null) {
            return CommonResult.error().message("参数错误");
        }
        int result = baseMapper.deleteBatchIds(Arrays.asList(ids));
        if (result > 0) {
            return CommonResult.ok();
        }
        return CommonResult.error().message("删除失败");
    }

    @Override
    public CommonResult deleteEmployeeById(Integer id) {

        if (id == null) {
            return CommonResult.error().message("参数错误");
        }
        int result = baseMapper.deleteById(id);
        if (result > 0) {
            return CommonResult.ok();
        }
        return CommonResult.error().message("删除失败");
    }

    @Override
    public CommonResult updateEmployee(Integer id, Employee employee) {

        if (id == null || employee == null) {
            return CommonResult.error().message("参数错误");
        }
        employee.setId(id);
        if (employee.getStoreId() != null && employee.getWarehouseId() != 0) {
            Employee employee1 = baseMapper.selectById(id);
            if (employee1 == null) {
                return CommonResult.error().message("员工不存在");
            }
            if (employee1.getStoreId() != 0) {
                employee.setStoreId(0);
            } else {
                employee.setWarehouseId(0);
            }
        }
        int result = baseMapper.updateById(employee);
        if (result > 0) {
            return CommonResult.ok();
        }
        return CommonResult.error().message("更新失败");
    }

    @Override
    public CommonResult addEmployee(Employee employee) {

        if (employee == null) {
            return CommonResult.error().message("参数错误");
        }
        if (employee.getStoreId() == null) {
            employee.setStoreId(0);
        }
        if (employee.getWarehouseId() == null) {
            employee.setWarehouseId(0);
        }

        int result = baseMapper.insert(employee);
        if (result > 0) {
            return CommonResult.ok();
        }
        return CommonResult.error().message("添加失败");
    }

    @Override
    public CommonResult getEmployeeListPageByStoreId(Integer currentPage, Integer pageSize, Integer storeId, EmployeeQuery employeeQuery) {
        if (currentPage == null || pageSize == null || storeId == null) {
            return CommonResult.error().message("参数错误");
        }
        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();
        employeeQueryWrapper.eq("store_id", storeId);
        employeeQueryWrapper.orderByDesc("gmt_modified");
        if (employeeQuery != null) {
            if (employeeQuery.getName() != null) {
                employeeQueryWrapper.like("name", employeeQuery.getName());
            }
            if (employeeQuery.getStartTime() != null) {
                employeeQueryWrapper.ge("gmt_create", employeeQuery.getStartTime());
            }
            if (employeeQuery.getEndTime() != null) {
                employeeQueryWrapper.le("gmt_create", employeeQuery.getEndTime());
            }
        }
        Page<Employee> employeePage = new Page<>();
        baseMapper.selectPage(employeePage, employeeQueryWrapper);
        long total = employeePage.getTotal();
        List<Employee> records = employeePage.getRecords();
        List<EmployeeVo> rows = getEmployeeVoList(records);
        return CommonResult.ok().data("total", total).data("rows", rows);
    }

    @Override
    public CommonResult getEmployeeListPageByWarehouseId(Integer currentPage, Integer pageSize, Integer warehouseId, EmployeeQuery employeeQuery) {
        if (currentPage == null || pageSize == null || warehouseId == null) {
            return CommonResult.error().message("参数错误");
        }
        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();
        employeeQueryWrapper.eq("warehouse_id", warehouseId);
        employeeQueryWrapper.orderByDesc("gmt_modified");
        if (employeeQuery != null) {
            if (employeeQuery.getName() != null) {
                employeeQueryWrapper.like("name", employeeQuery.getName());
            }
            if (employeeQuery.getStartTime() != null) {
                employeeQueryWrapper.ge("gmt_create", employeeQuery.getStartTime());
            }
            if (employeeQuery.getEndTime() != null) {
                employeeQueryWrapper.le("gmt_create", employeeQuery.getEndTime());
            }
        }
        Page<Employee> employeePage = new Page<>();
        baseMapper.selectPage(employeePage, employeeQueryWrapper);
        long total = employeePage.getTotal();
        List<Employee> records = employeePage.getRecords();
        List<EmployeeVo> rows = getEmployeeVoList(records);
        return CommonResult.ok().data("total", total).data("rows", rows);
    }

    private List<EmployeeVo> getEmployeeVoList(List<Employee> records) {
        List<Region> regionList = regionService.list();
        HashMap<Integer, Region> regionHashMap = new HashMap<>();
        regionList.forEach(region -> {
            regionHashMap.put(region.getId(), region);
        });
        HashMap<Integer, Store> storeHashMap = new HashMap<>();
        List<Store> storeList = storeMapper.selectList(null);
        storeList.stream().forEach(store -> {
            storeHashMap.put(store.getId(), store);
        });
        HashMap<Integer, Warehouse> warehouseHashMap = new HashMap<>();
        List<Warehouse> warehouseList = warehouseMapper.selectList(null);
        warehouseList.stream().forEach(warehouse -> {
            warehouseHashMap.put(warehouse.getId(), warehouse);
        });
        List<Position> positionList = positionMapper.selectList(null);
        HashMap<Integer, Position> positionHashMap = new HashMap<>();
        positionList.stream().forEach(position -> {
            positionHashMap.put(position.getId(), position);
        });

        List<EmployeeVo> rows = records.stream().map(employee -> {
            EmployeeVo employeeVo = new EmployeeVo();
            BeanUtils.copyProperties(employee, employeeVo);
            employeeVo.setPositionName(positionHashMap.get(employee.getPositionId()).getName());
            if (employee.getWarehouseId() != null && employee.getStoreId() != null && employee.getStoreId() == 0) {
                Warehouse warehouse = warehouseHashMap.get(employee.getWarehouseId());
                if (warehouse != null) {
                    employeeVo.setWarehouseRegion(regionService.getRegionName(warehouse.getRegionId(), regionHashMap));
                } else {
                    employeeVo.setWarehouseRegion("");
                }
            }
            if (employee.getStoreId() != null && employee.getWarehouseId() != null && employee.getWarehouseId() == 0) {
                Store store = storeHashMap.get(employee.getStoreId());
                if (store != null) {
                    employeeVo.setStoreRegion(regionService.getRegionName(store.getRegionId(), regionHashMap));
                } else {
                    employeeVo.setStoreRegion("");
                }
            }

            return employeeVo;
        }).collect(Collectors.toList());
        return rows;
    }
}




