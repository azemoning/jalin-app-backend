package com.jalin.jalinappbackend.seeder;

import com.jalin.jalinappbackend.module.gamification.mission.entity.Mission;
import com.jalin.jalinappbackend.module.gamification.mission.repository.MissionRepository;
import com.jalin.jalinappbackend.module.gamification.mission.service.MissionService;
import com.jalin.jalinappbackend.module.gamification.mission.service.UserMissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Order(value = 2)
@Slf4j
public class MissionSeeder implements CommandLineRunner {

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private MissionService missionService;

    @Override
    public void run(String... args) {
        try {
            seed();
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    private void seed() {
        if (missionRepository.count() == 0) {
            Mission mission1 = new Mission();
            mission1.setActivity("TOP_UP");
            mission1.setMissionDescription("Top up e-wallet");
            mission1.setTncDescription("Minimal transaksi Rp 50.000");
            mission1.setFrequency(5);
            mission1.setExpiration("WEEKLY");
            mission1.setPoint(250);
            mission1.setMinAmount(new BigDecimal(50000));

            missionService.addMission(mission1);

            Mission mission2 = new Mission();
            mission2.setActivity("TRANSFER");
            mission2.setMissionDescription("Transfer ke sesama bank");
            mission2.setTncDescription("Minimal transaksi Rp 50.000");
            mission2.setFrequency(5);
            mission2.setExpiration("BIWEEKLY");
            mission2.setPoint(250);
            mission2.setMinAmount(new BigDecimal(50000));

            missionService.addMission(mission2);

            Mission mission3 = new Mission();
            mission3.setActivity("TRANSFER_DOMESTIC");
            mission3.setMissionDescription("Transfer ke bank berbeda");
            mission3.setTncDescription("Minimal transaksi Rp 50.000");
            mission3.setFrequency(4);
            mission3.setExpiration("BIWEEKLY");
            mission3.setPoint(100);
            mission3.setMinAmount(new BigDecimal(50000));

            missionService.addMission(mission3);

            Mission mission4 = new Mission();
            mission4.setActivity("PAYMENT_BILL");
            mission4.setMissionDescription("Bayar tagihan (elektrik, air, telpon, internet)");
            mission4.setTncDescription("Tanpa minimal transaksi");
            mission4.setFrequency(2);
            mission4.setExpiration("MONTHLY");
            mission4.setPoint(100);
            mission4.setMinAmount(new BigDecimal(0));

            missionService.addMission(mission4);

            Mission mission5 = new Mission();
            mission5.setActivity("PAYMENT_MOBILE_PHONE_DATA");
            mission5.setMissionDescription("Isi internet quota");
            mission5.setTncDescription("Minimal transaksi Rp 20.000");
            mission5.setFrequency(2);
            mission5.setExpiration("WEEKLY");
            mission5.setPoint(40);
            mission5.setMinAmount(new BigDecimal(20000));

            missionService.addMission(mission5);

            Mission mission6 = new Mission();
            mission6.setActivity("PAYMENT_MOBILE_PHONE_CREDIT");
            mission6.setMissionDescription("Isi pulsa");
            mission6.setTncDescription("Minimal transaksi Rp 20.000");
            mission6.setFrequency(8);
            mission6.setExpiration("MONTHLY");
            mission6.setPoint(160);
            mission6.setMinAmount(new BigDecimal(20000));

            missionService.addMission(mission6);

            Mission mission7 = new Mission();
            mission7.setActivity("PAYMENT_QR");
            mission7.setMissionDescription("Payment QR with Jalin when shopping in merchant partner");
            mission7.setTncDescription("Minimal transaksi Rp 20.000");
            mission7.setFrequency(5);
            mission7.setExpiration("WEEKLY");
            mission7.setPoint(100);
            mission7.setMinAmount(new BigDecimal(20000));

            missionService.addMission(mission7);

            Mission mission8 = new Mission();
            mission8.setActivity("PAYMENT_QR");
            mission8.setMissionDescription("Payment QR with Jalin when shopping in merchant partner");
            mission8.setTncDescription("Minimal transaksi Rp 20.000");
            mission8.setFrequency(15);
            mission8.setExpiration("BIWEEKLY");
            mission8.setPoint(3000);
            mission8.setMinAmount(new BigDecimal(20000));

            missionService.addMission(mission8);

            Mission mission9 = new Mission();
            mission9.setActivity("PAYMENT_QR");
            mission9.setMissionDescription("Payment QR with Jalin when shopping in merchant partner");
            mission9.setTncDescription("Minimal transaksi Rp 20.000");
            mission9.setFrequency(30);
            mission9.setExpiration("MONTHLY");
            mission9.setPoint(6000);
            mission9.setMinAmount(new BigDecimal(20000));

            missionService.addMission(mission9);
        }
    }
}
