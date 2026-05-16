package com.dentalclinic.utils;

/**
 * URL trang chủ và trang làm việc theo vai trò đăng nhập.
 */
public final class RoleNavHelper {

    private RoleNavHelper() {
    }

    public static String getHomeUrl(String contextPath) {
        String ctx = contextPath != null ? contextPath : "";
        return ctx.isEmpty() ? "/" : ctx + "/";
    }

    public static String getWorkspaceUrl(String contextPath, String role) {
        String ctx = contextPath != null ? contextPath : "";
        if (role == null || role.trim().isEmpty()) {
            return ctx + "/hoso";
        }
        if ("Quản trị viên".equalsIgnoreCase(role.trim())) {
            return ctx + "/admin";
        }
        if ("Bác sĩ".equalsIgnoreCase(role.trim())) {
            return ctx + "/doctor/dashboard";
        }
        if ("Lễ tân".equalsIgnoreCase(role.trim())) {
            return ctx + "/reception-dashboard";
        }
        return ctx + "/hoso";
    }

    public static boolean isPatient(String role) {
        return role == null || role.trim().isEmpty() || "Bệnh nhân".equalsIgnoreCase(role.trim());
    }

    public static String getScheduleUrl(String contextPath) {
        return (contextPath != null ? contextPath : "") + "/Infor/Schedule";
    }

    public static String getServiceUrl(String contextPath) {
        return (contextPath != null ? contextPath : "") + "/Infor/service";
    }

    public static String getDoctorUrl(String contextPath) {
        return (contextPath != null ? contextPath : "") + "/Infor/Doctor";
    }
}
