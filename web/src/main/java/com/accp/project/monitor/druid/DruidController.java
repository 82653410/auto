package com.accp.project.monitor.druid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.accp.framework.web.controller.BaseController;

/**
 * druid 监控
 * 

 */
@Controller
@RequestMapping("/monitor/data")
public class DruidController extends BaseController
{

    @RequiresPermissions("monitor:data:view")
    @GetMapping()
    public String index()
    {
        String prefix = "/monitor/druid";
        return redirect(prefix + "/index");
    }
}
