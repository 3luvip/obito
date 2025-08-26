package com.girlkun.services.func;

import com.girlkun.database.GirlkunDB;
import com.girlkun.server.Manager;
import com.girlkun.utils.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TopService implements Runnable {

    private static TopService i;

    public static TopService gI() {
        if (i == null) {
            i = new TopService();
        }
        return i;
    }

    public static String gethotong() {
        StringBuffer sb = new StringBuffer("");

        String SELECT_TOP_POWER = "SELECT name, diemhotong FROM player ORDER BY diemhotong DESC LIMIT 10;";
        PreparedStatement ps;
        ResultSet rs;
        try {
            Connection conn = GirlkunDB.getConnection();
            ps = conn.prepareStatement(SELECT_TOP_POWER);
            conn.setAutoCommit(false);

            rs = ps.executeQuery();
            byte i = 1;
            while (rs.next()) {
                sb.append(i).append(".").append(rs.getString("name")).append(": ").append(rs.getString("diemhotong")).append("\b");
                i++;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static String gettopnaubanhtrung() {
        StringBuffer sb = new StringBuffer("");

        String SELECT_TOP_POWER = "SELECT name, diemnaubanhtrung FROM player ORDER BY diemnaubanhtrung DESC LIMIT 10;";
        PreparedStatement ps;
        ResultSet rs;
        try {
            Connection conn = GirlkunDB.getConnection();
            ps = conn.prepareStatement(SELECT_TOP_POWER);
            conn.setAutoCommit(false);

            rs = ps.executeQuery();
            byte i = 1;
            while (rs.next()) {
                sb.append(i).append(".").append(rs.getString("name")).append(": ").append(rs.getString("diemnaubanhtrung")).append("\b");
                i++;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
    
    public static String getpointNroNamec() {
        StringBuffer sb = new StringBuffer("");

        String SELECT_TOP_POWER = "SELECT name, pointNroNamec FROM player ORDER BY pointNroNamec DESC LIMIT 10;";
        PreparedStatement ps;
        ResultSet rs;
        try {
            Connection conn = GirlkunDB.getConnection();
            ps = conn.prepareStatement(SELECT_TOP_POWER);
            conn.setAutoCommit(false);

            rs = ps.executeQuery();
            byte i = 1;
            while (rs.next()) {
                sb.append(i).append(".").append(rs.getString("name")).append(": ").append(rs.getString("pointNroNamec")).append("\b");
                i++;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static String gettopsukienhe() {
        StringBuffer sb = new StringBuffer("");

        String SELECT_TOP_POWER = "SELECT name, diemsukienhe FROM player ORDER BY diemsukienhe DESC LIMIT 10;";
        PreparedStatement ps;
        ResultSet rs;
        try {
            Connection conn = GirlkunDB.getConnection();
            ps = conn.prepareStatement(SELECT_TOP_POWER);
            conn.setAutoCommit(false);

            rs = ps.executeQuery();
            byte i = 1;
            while (rs.next()) {
                sb.append(i)
                        .append(".")
                        .append(rs.getString("name"))
                        .append(": ")
                        .append(rs.getString("diemsukienhe"))
                        .append(" Điểm.")
                        .append("\b");

                i++;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (Manager.timeRealTop + (1000) < System.currentTimeMillis()) {
                    Manager.timeRealTop = System.currentTimeMillis();
                    try (Connection con = GirlkunDB.getConnection()) {
                        Manager.topSieuHang = Manager.realTopSieuHang(con);
                        Manager.topNV = Manager.realTop(Manager.queryTopNV, con);
                        Manager.topSM = Manager.realTop(Manager.queryTopSM, con);
                        Manager.topSK = Manager.realTop(Manager.queryTopSK, con);
                        Manager.topPVP = Manager.realTop(Manager.queryTopPVP, con);
                        Manager.topSD = Manager.realTop(Manager.queryTopSD, con);
                        Manager.topSieuHang = Manager.realTopSieuHang(con);
                    } catch (Exception ignored) {
                        Logger.error("Lỗi đọc top");
                    }
                }
                Thread.sleep(1000);
            } catch (Exception ignored) {
            }
        }
    }

}
