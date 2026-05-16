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

    /** Trang gộp: đặt lịch + dịch vụ + bác sĩ */
    public static String getInforPageUrl(String contextPath) {
        return (contextPath != null ? contextPath : "") + "/Infor/Schedule";
    }

    public static String getScheduleUrl(String contextPath) {
        return getInforPageUrl(contextPath) + "#datlich";
    }

    public static String getServiceUrl(String contextPath) {
        return getInforPageUrl(contextPath) + "#dichvu";
    }

    public static String getDoctorUrl(String contextPath) {
        return getInforPageUrl(contextPath) + "#bacsi";
    }

    public static String getWorkspaceLabel(String role) {
        if (role == null) {
            return "Trang làm việc";
        }
        if ("Quản trị viên".equalsIgnoreCase(role.trim())) {
            return "Trang quản trị";
        }
        if ("Bác sĩ".equalsIgnoreCase(role.trim())) {
            return "Trang bác sĩ";
        }
        if ("Lễ tân".equalsIgnoreCase(role.trim())) {
            return "Trang lễ tân";
        }
        return "Trang làm việc";
    }
}
