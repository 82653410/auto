package com.accp.project.system.dict.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.accp.common.utils.poi.ExcelUtil;
import com.accp.framework.aspectj.lang.annotation.Log;
import com.accp.framework.aspectj.lang.enums.BusinessType;
import com.accp.framework.web.controller.BaseController;
import com.accp.framework.web.domain.AjaxResult;
import com.accp.framework.web.page.TableDataInfo;
import com.accp.project.system.dict.domain.DictData;
import com.accp.project.system.dict.service.IDictDataService;

/**
 * 数据字典信息
 * 
 
 */
@Controller
@RequestMapping("/system/dict/data")
public class DictDataController extends BaseController
{
    @Autowired
    private IDictDataService dictDataService;

    @RequiresPermissions("system:dict:view")
    @GetMapping()
    public String dictData()
    {
        return "system/dict/data/data";
    }

    @PostMapping("/list")
    @RequiresPermissions("system:dict:list")
    @ResponseBody
    public TableDataInfo list(DictData dictData)
    {
        startPage();
        List<DictData> list = dictDataService.selectDictDataList(dictData);
        return getDataTable(list);
    }

    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:dict:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(DictData dictData)
    {
        List<DictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil<DictData> util = new ExcelUtil<>(DictData.class);
        return util.exportExcel(list, "字典数据");
    }

    /**
     * 新增字典类型
     */
    @GetMapping("/add/{dictType}")
    public String add(@PathVariable("dictType") String dictType, ModelMap mmap)
    {
        mmap.put("dictType", dictType);
        return "system/dict/data/add";
    }

    /**
     * 新增保存字典类型
     */
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:dict:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(DictData dict)
    {
        return toAjax(dictDataService.insertDictData(dict));
    }

    /**
     * 修改字典类型
     */
    @GetMapping("/edit/{dictCode}")
    public String edit(@PathVariable("dictCode") Long dictCode, ModelMap mmap)
    {
        mmap.put("dict", dictDataService.selectDictDataById(dictCode));
        return "system/dict/data/edit";
    }

    /**
     * 修改保存字典类型
     */
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:dict:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(DictData dict)
    {
        return toAjax(dictDataService.updateDictData(dict));
    }

    @Log(title = "字典数据", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:dict:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(dictDataService.deleteDictDataByIds(ids));
    }
  
	/**
	 * Ajax前台获取操作字典
	 * @param stepType 步骤类型
	 * @return 返回步骤类型集合
	 *
	 *
	 */
    @GetMapping("/getDictDataListByStepType/{stepType}")
    @ResponseBody
	public String getDictDataListByStepType(@PathVariable("stepType") Integer stepType) {
		String str = "{\"message\": \"\",\"value\": ,\"code\": 200,\"redirect\": \"\" }";
		try {
			JSONObject json;
			DictData dictData=new DictData();
			if(stepType==0){
				dictData.setDictType("testmanagmt_casestep_httpoperation");
			}else if(stepType==1){
				dictData.setDictType("testmanagmt_casestep_uioperation");
			}else if(stepType==3){
				dictData.setDictType("testmanagmt_casestep_muioperation");
			}else{
				dictData.setDictType("9999999999999999999999999999999999");
			}
			List<DictData> ddlist = dictDataService.selectDictDataList(dictData);
			JSONArray jsonarr = new JSONArray();
			for(DictData obdd:ddlist){
				JSONObject jo = new JSONObject();
                jo.put("name", obdd.getDictValue());
                jo.put("description", obdd.getRemark());
                jsonarr.add(jo);
			}
			String recordJson = jsonarr.toString();
					
			json = JSONObject.parseObject("{\"message\": \"\",\"value\": "+recordJson+",\"code\": 200,\"redirect\": \"\" }");
			str=json.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
    
}
