package com.winning.mobileclinical.action;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.model.MenuDTO;
import com.winning.mobileclinical.model.cis.PatientInfo;

import java.util.List;

public class MenuAction {
    /**
     * 获得主界面导航栏菜单
     *
     * @param patient
     */
    public static List<MenuDTO> getMenuList(Context context, PatientInfo patient) {
        //加载一次之后存到全局变量中
        List<MenuDTO> menulist = GlobalCache.getCache().getMenuList();
        String jsonString = "[{\"id\":\"1\",\"text\":\"患者列表\",\"note\":\"患者列表\",\"suptype\":0,\"intent\":\"com.winning.mobileclinical.fragment.PatientList\",\"orderby\":0,\"state\":2,\"lcljbz\":\"0\",\"imageurl\":\"patient\"},{\"id\":\"2\",\"text\":\"手术查询\",\"note\":\"手术查询\",\"suptype\":0,\"intent\":\"com.winning.mobileclinical.fragment.OperationList\",\"orderby\":1,\"state\":2,\"lcljbz\":\"2\",\"imageurl\":\"operation\"},{\"id\":\"3\",\"text\":\"  实时提醒\",\"note\":\"实时提醒\",\"suptype\":0,\"intent\":\"com.winning.mobileclinical.fragment.RemindList\",\"orderby\":2,\"state\":2,\"lcljbz\":\"0\",\"imageurl\":\"warn\"},{\"id\":\"4\",\"text\":\"    我的    \",\"note\":\"  我的信息    \",\"suptype\":0,\"intent\":\"com.winning.mobileclinical.fragment.DoctorList\",\"orderby\":3,\"state\":2,\"lcljbz\":\"0\",\"imageurl\":\"my\"}]";
        try {
            if (jsonString != null) {
                Gson gson = new Gson();
                menulist = gson.fromJson(jsonString,
                        new TypeToken<List<MenuDTO>>() {
                        }.getType());
            }
        } catch (JsonIOException e) {
            e.printStackTrace();
        }
        return menulist;
    }

    /**
     * 获得人界面主导航栏菜单
     *
     * @param patient
     */
    public static List<MenuDTO> getMenuListPatient(Context context, PatientInfo patient) {
        //加载一次之后存到全局变量中
        List<MenuDTO> menulist = GlobalCache.getCache().getMenuList();

//		String jsonString = HTTPGetTool.getTool().getJsonString(context,
//				WebUtilsHOST+WebUtils.MENUACTION);

        String jsonString = "[{\"id\":\"1\",\"text\":\"患者列表\",\"note\":\"患者列表\",\"suptype\":1,\"intent\":\"com.winning.mobileclinical.fragment.PatientList\",\"orderby\":0,\"state\":2,\"lcljbz\":\"0\",\"imageurl\":\"patient\"},{\"id\":\"2\",\"text\":\"手术查询\",\"note\":\"手术查询\",\"suptype\":0,\"intent\":\"com.winning.mobileclinical.fragment.OperationList\",\"orderby\":1,\"state\":2,\"lcljbz\":\"2\",\"imageurl\":\"operation\"},{\"id\":\"3\",\"text\":\"实时提醒\",\"note\":\"实时提醒\",\"suptype\":0,\"intent\":\"com.winning.mobileclinical.fragment.RemindList\",\"orderby\":2,\"state\":2,\"lcljbz\":\"0\",\"imageurl\":\"warn\"},{\"id\":\"4\",\"text\":\"我的\",\"note\":\"我的\",\"suptype\":0,\"intent\":\"com.winning.mobileclinical.fragment.DoctorList\",\"orderby\":3,\"state\":2,\"lcljbz\":\"0\",\"imageurl\":\"my\"}]";

        try {
            if (jsonString != null) {
                Gson gson = new Gson();
                menulist = gson.fromJson(jsonString,
                        new TypeToken<List<MenuDTO>>() {
                        }.getType());
            }
        } catch (JsonIOException e) {
            e.printStackTrace();
        }


        System.out.println("menusize=" + menulist.size());

        return menulist;
    }

    /**
     * 获得患者导航栏菜单
     *
     * @param patient
     */
    public static List<MenuDTO> getTabMenuList(Context context, PatientInfo patient) {

        //加载一次之后存到全局变量中
        List<MenuDTO> menulist = GlobalCache.getCache().getMenuList();

//		String jsonString = HTTPGetTool.getTool().getJsonString(context,
//				WebUtilsHOST+WebUtils.MENUACTION);
        String jsonString = "";
        if (patient.getLcljbz() == 1) {
            jsonString = "[{\"id\":\"1\",\"text\":\"病历\",\"note\":\"病历\",\"suptype\":0,\"intent\":\"com.winning.mobileclinical.fragment.CaseHistory\",\"orderby\":0,\"state\":2,\"lcljbz\":\"0\",\"imageurl\":\"patient\"},{\"id\":\"2\",\"text\":\"医嘱\",\"note\":\"医嘱\",\"suptype\":0,\"intent\":\"com.winning.mobileclinical.fragment.OrdersMessage\",\"orderby\":1,\"state\":2,\"lcljbz\":\"2\",\"imageurl\":\"concern\"},{\"id\":\"3\",\"text\":\"检查\",\"note\":\"检查\",\"suptype\":0,\"intent\":\"com.winning.mobileclinical.fragment.MedicalReportsRIS\",\"orderby\":2,\"state\":2,\"lcljbz\":\"0\",\"imageurl\":\"warn\"},{\"id\":\"4\",\"text\":\"检验\",\"note\":\"检验\",\"suptype\":0,\"intent\":\"com.winning.mobileclinical.fragment.MedicalReportsLIS\",\"orderby\":3,\"state\":2,\"lcljbz\":\"0\",\"imageurl\":\"warn\"},{\"id\":\"5\",\"text\":\"临床路径\",\"note\":\"临床路径\",\"suptype\":0,\"intent\":\"com.winning.mobileclinical.fragment.LCLJMessage\",\"orderby\":4,\"state\":2,\"lcljbz\":\"0\",\"imageurl\":\"my\"},{\"id\":\"6\",\"text\":\"护理\",\"note\":\"护理\",\"suptype\":0,\"intent\":\"com.winning.mobileclinical.fragment.TempRecords\",\"orderby\":5,\"state\":2,\"lcljbz\":\"0\",\"imageurl\":\"my\"}]";
        } else {
            jsonString = "[{\"id\":\"1\",\"text\":\"病历\",\"note\":\"病历\",\"suptype\":0,\"intent\":\"com.winning.mobileclinical.fragment.CaseHistory\",\"orderby\":0,\"state\":2,\"lcljbz\":\"0\",\"imageurl\":\"patient\"},{\"id\":\"2\",\"text\":\"医嘱\",\"note\":\"医嘱\",\"suptype\":0,\"intent\":\"com.winning.mobileclinical.fragment.OrdersMessage\",\"orderby\":1,\"state\":2,\"lcljbz\":\"2\",\"imageurl\":\"concern\"},{\"id\":\"3\",\"text\":\"检查\",\"note\":\"检查\",\"suptype\":0,\"intent\":\"com.winning.mobileclinical.fragment.MedicalReportsRIS\",\"orderby\":2,\"state\":2,\"lcljbz\":\"0\",\"imageurl\":\"warn\"},{\"id\":\"4\",\"text\":\"检验\",\"note\":\"检验\",\"suptype\":0,\"intent\":\"com.winning.mobileclinical.fragment.MedicalReportsLIS\",\"orderby\":3,\"state\":2,\"lcljbz\":\"0\",\"imageurl\":\"warn\"},{\"id\":\"6\",\"text\":\"护理\",\"note\":\"护理\",\"suptype\":0,\"intent\":\"com.winning.mobileclinical.fragment.TempRecords\",\"orderby\":5,\"state\":2,\"lcljbz\":\"0\",\"imageurl\":\"my\"}]";
        }

        try {
            if (jsonString != null) {
                Gson gson = new Gson();
                menulist = gson.fromJson(jsonString,
                        new TypeToken<List<MenuDTO>>() {
                        }.getType());
            }
        } catch (JsonIOException e) {
            e.printStackTrace();
        }

        return menulist;
    }
}
