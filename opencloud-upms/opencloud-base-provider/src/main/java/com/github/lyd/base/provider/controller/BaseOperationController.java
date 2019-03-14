package com.github.lyd.base.provider.controller;

import com.github.lyd.base.client.model.BaseResourceOperationDto;
import com.github.lyd.base.client.model.entity.BaseResourceOperation;
import com.github.lyd.base.provider.service.BaseResourceOperationService;
import com.github.lyd.common.http.OpenRestTemplate;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.common.model.ResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liuyadu
 */
@Api(tags = "系统操作资源管理")
@RestController
public class BaseOperationController {
    @Autowired
    private BaseResourceOperationService baseResourceOperationService;
    @Autowired
    private OpenRestTemplate openRestTemplate;

    /**
     * 获取分页操作列表
     *
     * @return
     */
    @ApiOperation(value = "获取分页操作列表", notes = "获取分页操作列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "显示条数:最大999", paramType = "form"),
            @ApiImplicitParam(name = "keyword", value = "查询字段", paramType = "form"),
    })
    @PostMapping("/operation")
    public ResultBody<PageList<BaseResourceOperationDto>> getOperationListPage(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "keyword", required = false) String keyword
    ) {
        return ResultBody.success(baseResourceOperationService.findListPage(new PageParams(page, limit), keyword));
    }


    /**
     * 获取菜单下所有操作
     *
     * @param menuId
     * @return
     */
    @ApiOperation(value = "获取菜单下所有操作", notes = "获取菜单下所有操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "查询字段", paramType = "form"),
    })
    @PostMapping("/operation/menu")
    public ResultBody<List<BaseResourceOperationDto>> getOperationAllList(Long menuId) {
        return ResultBody.success(baseResourceOperationService.findListByMenuId(menuId));
    }

    /**
     * 获取操作资源详情
     *
     * @param operationId
     * @return
     */
    @ApiOperation(value = "获取操作资源详情", notes = "获取操作资源详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "operationId", required = true, value = "操作Id", paramType = "path"),
    })
    @GetMapping("/operation/{operationId}")
    public ResultBody<BaseResourceOperationDto> getOperation(@PathVariable("operationId") Long operationId) {
        return ResultBody.success(baseResourceOperationService.getOperation(operationId));
    }

    /**
     * 添加操作资源
     *
     * @param operationCode 操作编码
     * @param operationName 操作名称
     * @param menuId        上级菜单
     * @param apiId          绑定接口
     * @param status        是否启用
     * @param priority      优先级越小越靠前
     * @param operationDesc 描述
     * @return
     */
    @ApiOperation(value = "添加操作资源", notes = "添加操作资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "operationCode", required = true, value = "操作编码", paramType = "form"),
            @ApiImplicitParam(name = "operationName", required = true, value = "操作名称", paramType = "form"),
            @ApiImplicitParam(name = "menuId", required = true, value = "上级菜单", paramType = "form"),
            @ApiImplicitParam(name = "apiId", required = false, value = "绑定接口", paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, defaultValue = "1", allowableValues = "0,1", value = "是否启用", paramType = "form"),
            @ApiImplicitParam(name = "priority", required = false, value = "优先级越小越靠前", paramType = "form"),
            @ApiImplicitParam(name = "operationDesc", required = false, value = "描述", paramType = "form"),
    })
    @PostMapping("/operation/add")
    public ResultBody<Long> addOperation(
            @RequestParam(value = "operationCode") String operationCode,
            @RequestParam(value = "operationName") String operationName,
            @RequestParam(value = "menuId") Long menuId,
            @RequestParam(value = "apiId", required = false) Long apiId,
            @RequestParam(value = "status", defaultValue = "1") Integer status,
            @RequestParam(value = "priority", required = false, defaultValue = "0") Integer priority,
            @RequestParam(value = "operationDesc", required = false, defaultValue = "") String operationDesc
    ) {
        BaseResourceOperation operation = new BaseResourceOperation();
        operation.setOperationCode(operationCode);
        operation.setOperationName(operationName);
        operation.setMenuId(menuId);
        operation.setApiId(apiId);
        operation.setStatus(status);
        operation.setPriority(priority);
        operation.setOperationDesc(operationDesc);
        Long result = baseResourceOperationService.addOperation(operation);
        return ResultBody.success(result);
    }

    /**
     * 编辑操作资源
     *
     * @param operationId   操作ID
     * @param operationCode 操作编码
     * @param operationName 操作名称
     * @param menuId        上级菜单
     * @param apiId         绑定接口
     * @param status        是否启用
     * @param priority      优先级越小越靠前
     * @param operationDesc 描述
     * @return
     */
    @ApiOperation(value = "编辑操作资源", notes = "添加操作资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "operationId", required = true, value = "操作ID", paramType = "form"),
            @ApiImplicitParam(name = "operationCode", required = true, value = "操作编码", paramType = "form"),
            @ApiImplicitParam(name = "operationName", required = true, value = "操作名称", paramType = "form"),
            @ApiImplicitParam(name = "menuId", required = true, value = "上级菜单", paramType = "form"),
            @ApiImplicitParam(name = "apiId", required = false, value = "绑定接口", paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, defaultValue = "1", allowableValues = "0,1", value = "是否启用", paramType = "form"),
            @ApiImplicitParam(name = "priority", required = false, value = "优先级越小越靠前", paramType = "form"),
            @ApiImplicitParam(name = "operationDesc", required = false, value = "描述", paramType = "form"),
    })
    @PostMapping("/operation/update")
    public ResultBody updateOperation(
            @RequestParam("operationId") Long operationId,
            @RequestParam(value = "operationCode") String operationCode,
            @RequestParam(value = "operationName") String operationName,
            @RequestParam(value = "menuId") Long menuId,
            @RequestParam(value = "apiId", required = false, defaultValue = "") Long apiId,
            @RequestParam(value = "status", defaultValue = "1") Integer status,
            @RequestParam(value = "priority", required = false, defaultValue = "0") Integer priority,
            @RequestParam(value = "operationDesc", required = false, defaultValue = "") String operationDesc
    ) {
        BaseResourceOperation operation = new BaseResourceOperation();
        operation.setOperationId(operationId);
        operation.setOperationCode(operationCode);
        operation.setOperationName(operationName);
        operation.setMenuId(menuId);
        operation.setApiId(apiId);
        operation.setStatus(status);
        operation.setPriority(priority);
        operation.setOperationDesc(operationDesc);
        baseResourceOperationService.updateOperation(operation);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultBody.success();
    }


    /**
     * 移除操作资源
     *
     * @param operationId
     * @return
     */
    @ApiOperation(value = "移除操作资源", notes = "移除操作资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "operationId", required = true, value = "操作ID", paramType = "form")
    })
    @PostMapping("/operation/remove")
    public ResultBody removeOperation(
            @RequestParam("operationId") Long operationId
    ) {
        baseResourceOperationService.removeOperation(operationId);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultBody.success();
    }
}
