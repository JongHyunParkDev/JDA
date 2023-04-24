package com.yj.yjbot.data;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Queue;

public class Gift {
    private String gift;

    private String message;

    private String message2;

    private int startHour;

    public String getGift() {
        return gift;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage2() {
        return message2;
    }

    public int getStartHour() {
        return startHour;
    }

    public Gift(String gift, String message, String message2, int startHour) {
        this.gift = gift;
        this.message = message;
        this.message2 = message2;
        this.startHour = startHour;
    }

    public String getDiscordText() {
        String result = "";
        result += gift + "\n" + message;
        if (message2 != null)
            result += "\n" + message2;
        return result;
    }

    public static final LocalDateTime START_DATE_TIME =
            LocalDateTime.of(2023, 4, 25, 0, 0, 0);

    public static LocalDateTime END_DATE_TIME =
            LocalDateTime.of(2023, 4, 26, 0, 0, 0);

    public static final Queue<Gift> GIFT_LIST = new ArrayDeque<>() {{
        add(new Gift("선물: 초콜렛과 꽃",
                "양미 생일 축하해! 맛있던 초콜렛과 꽃(안개)이당!",
                "이번에 밀크랑 말차는 2개니까 선생님이 가져가서 먹어랑!",
                0
        ));
        add(new Gift("선물: 향수 (라모수 향수! (끌로 향))",
                "브랜드가 아니지만.. 별로 좋아하지 않는 걸로 알고 있어서... 만약 좋으면 브랜드꺼 사줄께!",
                null,
                8
        ));
        add(new Gift("선물: 반지 (날씨 반지야! (13호))",
                "호수가 잘 맞으면 좋겠어! 제바류ㅠㅠㅠ",
                "It's not what the weather is, what the environment is, \n" +
                        "but what your mind thinks is the most important.\n" +
                        "And I hope your mind is \"Always Sunny\"",
                13
        ));
        add(new Gift("선물: 네컷편지",
                "편지가 없으면 섭하지!",
                "TMI - 흠... 글자 굵기가 너무 크네... 적당한 네임판 살껄...",
                18
        ));
        add(new Gift("선물: 액자",
                "마지막 선물인데... 모든 선물이 좋았으면 좋겠땅!",
                "앵미랑 첫 여행 갔을 때 사진이야. 먼가 둘다 이쁘게 잘 나온 거 같고, 둘다 잘 차려 입어서 선택했어!",
                21
        ));
    }};
}
