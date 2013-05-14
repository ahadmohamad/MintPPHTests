package com.paypal.test.riskmint.pph.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.paypal.test.jaws.db.DatabaseName;
import com.paypal.test.jaws.db.JDBCUtils;

public class DBOperation {

    private static JDBCUtils riskDB = null;
    private static ResultSet rs = null;

    public static boolean wruleLog(String accountNumber, int ruleID, int action, String checkPoint) {

        String deleteHistorySQL = "delete FROM wrule_log WHERE rule_id = ruleID";
        String searchSQL = "SELECT count(*) count FROM wrule_log WHERE  action = " + action + " and rule_id in ("
                + ruleID + ") and checkpoint_log_id in (SELECT id FROM wrule_checkpoint_log where account_number = "
                + accountNumber + " and checkpoint = '" + checkPoint + "' )";

        System.out.println("wruleLog SQL search string is: " + searchSQL + "\n");
        boolean status = false;
        try {
            riskDB = new JDBCUtils(DatabaseName.RISK);
            Thread.sleep(3000);
            riskDB.executeQuery(deleteHistorySQL);
            rs = riskDB.executeQuery(searchSQL);
            while(rs.next()) {
                assertEquals(rs.getInt("count"), 1, "actions are not equal");
                status = true;
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
            fail("Error performing SQL query in wruleLog");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception in DB call to fetch wruleLog data \n");
        }

        finally {
            try {
                riskDB.closeConnection();
                rs = null;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return status;
    }

    public static boolean getTxnDataFromWriskStats(String accountNumber, boolean isSwipe, int amount) {
        String checkSQL = " select account_number,KB_NON_SWIPE_AMT_LAST_30_DAYS,KB_SWIPE_AMT_LAST_30_DAYS,KB_NON_SWIPE_AMT_LATEST_DAY,KB_SWIPE_AMT_LATEST_DAY from wrisk_stats where account_number = "
                + accountNumber;

        System.out.println("wriskstats SQL string is: " + checkSQL + "\n");
        boolean status = false;
        try {
            Thread.sleep(200000);
            riskDB = new JDBCUtils(DatabaseName.RUSER);
            rs = riskDB.executeQuery(checkSQL);
            while(rs.next()) {
                if(isSwipe) {
                    if(rs.getString("KB_SWIPE_AMT_LAST_30_DAYS")!=null) {
                        status = true;
                    }
                } else {
                    if(rs.getString("KB_NON_SWIPE_AMT_LAST_30_DAYS") != null) {
                        status = true;
                    }
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error performing SQL query in wriskRiskStat-cc");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception in DB call to fetch wriskRiskStat\n");
        } finally {
            try {
                riskDB.closeConnection();
                rs = null;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return status;
    }

    public static void cleanWriskStats(String accountNumber) {
        String deleteSQL = "delete from wrisk_stats where account_number = " + accountNumber;

        System.out.println("wriskstats SQL string is: " + deleteSQL + "\n");
        try {
            riskDB = new JDBCUtils(DatabaseName.RUSER);
            riskDB.executeQuery(deleteSQL);

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error performing SQL query in wriskRiskStat-cc");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception in DB call to fetch wriskRiskStat\n");
        } finally {
            try {
                riskDB.closeConnection();
                rs = null;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
}