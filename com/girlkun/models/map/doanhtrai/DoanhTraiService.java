package com.girlkun.models.map.doanhtrai;

import com.girlkun.models.player.Player;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.models.map.Zone;
import com.girlkun.services.Service;
import com.girlkun.utils.Logger;
import java.util.ArrayList;
import java.util.List;
import com.girlkun.models.boss.list_boss.DoanhTraiDocNhan.RobotVeSi;
import com.girlkun.models.boss.list_boss.DoanhTraiDocNhan.TrungUyTrang;
import com.girlkun.models.boss.list_boss.DoanhTraiDocNhan.NinjaAoTim;
import com.girlkun.models.boss.list_boss.DoanhTraiDocNhan.TrungUyThep;
import com.girlkun.models.boss.list_boss.DoanhTraiDocNhan.TrungUyXanhLo;
/**
 *
 * @author BTH
 *
 */
public class DoanhTraiService {

    private static DoanhTraiService I;

    public static DoanhTraiService gI() {
        if (DoanhTraiService.I == null) {
            DoanhTraiService.I = new DoanhTraiService();
        }
        return DoanhTraiService.I;
    }

    public List<DoanhTrai> doanhTrais;

    private DoanhTraiService() {
        this.doanhTrais = new ArrayList<>();
        for (int i = 0; i < DoanhTrai.AVAILABLE; i++) {
            this.doanhTrais.add(new DoanhTrai(i));
        }
    }

    public void addMapDoanhTrai(int id, Zone zone) {
        this.doanhTrais.get(id).getZones().add(zone);
    }

    public void joinDoanhTrai(Player pl) throws Exception {
        if (pl.clan == null) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
            return;
        }
        if (pl.clan.doanhTrai != null) {
            ChangeMapService.gI().changeMapInYard(pl, 53, -1, 60);
            return;
        }
        DoanhTrai doanhTrai = null;
        for (DoanhTrai dt : this.doanhTrais) {
            if (dt.getClan() == null) {
                doanhTrai = dt;
                break;
            }
        }
        if (doanhTrai != null) {
            doanhTrai.openDoanhTrai(pl);
try {
                        long totalDame = 0;
                        long totalHp = 0;
                        for (Player play : pl.clan.membersInGame) {
                            totalDame += play.nPoint.dame;
                            totalHp += play.nPoint.hpMax;
                        }
                            long dame = ( totalHp / 50) ;
                            long hp = (  totalDame  * 300);
                            if (dame >= 2000000000L) {
                                dame = 2000000000L;
                            }
                            if (hp >= 2000000000L) {
                                hp = 2000000000L;
                            }
                              long bossDamage = dame;
                           long bossMaxHealth = hp ;
                           bossDamage = Math.min(bossDamage, 200000000L);
                           bossMaxHealth = Math.min(bossMaxHealth, 2000000000L);
                            new TrungUyTrang(pl.clan.doanhTrai.getMapById(59), (int) dame, (int) hp);
                            new TrungUyXanhLo(pl.clan.doanhTrai.getMapById(62), (int) dame, (int) hp);
                            new TrungUyThep(pl.clan.doanhTrai.getMapById(55), (int) dame, (int) hp);
                            new NinjaAoTim(pl.clan.doanhTrai.getMapById(54), (int) dame, (int) hp);
                            new RobotVeSi(pl.clan.doanhTrai.getMapById(57), (int) dame, (int) hp);
                            new RobotVeSi(pl.clan.doanhTrai.getMapById(57), (int) dame, (int) hp);
                            new RobotVeSi(pl.clan.doanhTrai.getMapById(57), (int) dame, (int) hp);
                            new RobotVeSi(pl.clan.doanhTrai.getMapById(57), (int) dame, (int) hp);
                        } catch (Exception e) {
                            Logger.logException(DoanhTraiService.class, e, "Lỗi init boss");
                        }
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                  }
               }
