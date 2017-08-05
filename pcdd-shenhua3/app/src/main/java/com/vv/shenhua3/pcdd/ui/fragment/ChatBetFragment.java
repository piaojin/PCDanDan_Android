package com.vv.shenhua3.pcdd.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.EaseChatInputMenu;
import com.hyphenate.easeui.widget.SimpleCountDownTextView;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.entity.BettingJson;
import com.vv.shenhua3.pcdd.network.ApiInterface;
import com.vv.shenhua3.pcdd.network.HttpResultCallback;
import com.vv.shenhua3.pcdd.network.MySubcriber;
import com.vv.shenhua3.pcdd.network.bean.BetDetailInfo;
import com.vv.shenhua3.pcdd.network.bean.GameOddsInfo;
import com.vv.shenhua3.pcdd.network.bean.GameTypeInfo;
import com.vv.shenhua3.pcdd.network.request.BettingRequest;
import com.vv.shenhua3.pcdd.network.request.ExitRoomRequest;
import com.vv.shenhua3.pcdd.network.request.GameTypeDataRequest;
import com.vv.shenhua3.pcdd.network.request.JoinRoomRequest;
import com.vv.shenhua3.pcdd.network.request.RoomBetDetailRequest;
import com.vv.shenhua3.pcdd.ui.RechargeActivity;
import com.vv.shenhua3.pcdd.ui.widget.FormulaTextView;
import com.vv.shenhua3.pcdd.ui.widget.bet.CustomChatRowProvider;
import com.vv.shenhua3.pcdd.ui.widget.dialog.BettingOddsDlg;
import com.vv.shenhua3.pcdd.ui.widget.dialog.ChatBetFlowDlg;
import com.vv.shenhua3.pcdd.ui.widget.dialog.LotteryLogDialog;
import com.vv.shenhua3.pcdd.util.Arith;
import com.vv.shenhua3.pcdd.util.CheckUtil;
import com.vv.shenhua3.pcdd.util.ProgressDialogUtil;
import com.vv.shenhua3.pcdd.util.T;

import java.util.List;

/**
 * Created by hang on 2017/2/20.
 */

public class ChatBetFragment extends EaseChatFragment implements BettingOddsDlg.BetOddsCallback {

    private LinearLayout llBetResult;
    private TextView tvCurGameNum;
    private TextView tvPreGameNum;
    private SimpleCountDownTextView tvCountDown;
    private TextView tvMyPoint;
    private FormulaTextView tvBetFormula;
    private TextView tvBetResultType;
    private LinearLayout llLotteryLog;
    private LinearLayout llRefreshPoint;

    private Activity activity;

    private double userPoint;
    private int roomId;
    private int areaId;
    private int gameType;   ////1北京快乐8 2加拿大快乐8
    private double minPoint;  //个人下注下限
    private double maxPoint;  //个人下注上限

    private CountDownTimer openingTimer; //开盘倒计时

    private BettingOddsDlg oddsDlg;

    private EMMessageListener emMessageListener;

    private boolean isActive;

    private int status; //当前开盘状态 1正常 2封盘 3停售
    private String curGameNum; //当前期数

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isActive = true;
        roomId = getArguments().getInt("roomId", 0);
        areaId = getArguments().getInt("areaId", 0);
        gameType = getArguments().getInt("gameType", 1);
        minPoint = getArguments().getDouble("minPoint", 0);
        maxPoint = getArguments().getDouble("minPoint", 100000);

        EaseUI.getInstance().setSettingsProvider(new EaseUI.EaseSettingsProvider() {
            @Override
            public boolean isMsgNotifyAllowed(EMMessage message) {
                return true;
            }

            @Override
            public boolean isMsgSoundAllowed(EMMessage message) {
                return false;
            }

            @Override
            public boolean isMsgVibrateAllowed(EMMessage message) {
                return false;
            }

            @Override
            public boolean isSpeakerOpened() {
                return true;
            }
        });
    }

    @Override
    protected void setUpView() {
        activity = getActivity();
        chatType = EaseConstant.CHATTYPE_GROUP;

        llBetResult = (LinearLayout) getView().findViewById(R.id.llBetResult);
        tvCurGameNum = (TextView) getView().findViewById(R.id.tvCurGameNum);
        tvPreGameNum = (TextView) getView().findViewById(R.id.tvPreGameNum);
        tvCountDown = (SimpleCountDownTextView) getView().findViewById(R.id.tvCountDown);
        tvMyPoint = (TextView) getView().findViewById(R.id.tvMyPoint);
        tvBetFormula = (FormulaTextView) getView().findViewById(R.id.tvBetFormula);
        tvBetResultType = (TextView) getView().findViewById(R.id.tvBetResultType);
        llLotteryLog = (LinearLayout) getView().findViewById(R.id.llLotteryLog);
        llRefreshPoint = (LinearLayout) getView().findViewById(R.id.llRefreshPoint);

        initListener();

        super.setUpView();
        titleBar.setTitle(fragmentArgs.getString("title"));
        titleBar.setRightImageResource(0);
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        titleBar.rightImage.setImageResource(R.drawable.btn_cus_svr);
        titleBar.rightImage2.setImageResource(R.drawable.btn_flow);

        loadBetDetail(null, 0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sendJoinedMsg();
            }
        }, 500);
    }

    @Override
    protected void registerExtendMenuItem() {
//        for(int i = 0; i < 2; i++){
//            inputMenu.registerExtendMenuItem(itemStrings[i], itemdrawables[i], itemIds[i], extendMenuItemClickListener);
//        }
    }

    public void initListener() {
        emMessageListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                for(EMMessage message : list) {
                    EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                    BettingJson json = new Gson().fromJson(txtBody.getMessage(), BettingJson.class);
                    if(json.notice_type == 3) {
                        loadBetDetail(null, 0);
                        return;
                    }
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {
            }

            @Override
            public void onMessageRead(List<EMMessage> list) {
            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {
            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(emMessageListener);

        inputMenu.setChatInputMenuListener(new EaseChatInputMenu.ChatInputMenuListener() {
            @Override
            public void onSendMessage(String content) {
                T.showShort("系统已禁用发言功能");
            }

            @Override
            public void onBigExpressionClicked(EaseEmojicon emojicon) {
            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                return false;
            }
        });
        inputMenu.setOnBetListener(new EaseChatInputMenu.OnBetListener() {
            @Override
            public void onBetBtnClicked() {
                if(oddsDlg == null)
                    loadOddsData();
                else
                    oddsDlg.show(getChildFragmentManager(), "");
            }
        });

        setChatFragmentListener(new EaseChatFragmentHelper() {
            @Override
            public void onSetMessageAttributes(EMMessage message) {
            }

            @Override
            public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
                return new CustomChatRowProvider(activity);
            }

            @Override
            public void onMessageBubbleLongClick(EMMessage message) {
            }

            @Override
            public boolean onMessageBubbleClick(EMMessage message) {
                handleBubbleClick(message);
                return true;
            }

            @Override
            public boolean onExtendMenuItemClick(int itemId, View view) {
                return false;
            }

            @Override
            public void onEnterToChatDetails() {
            }

            @Override
            public void onAvatarLongClick(String username) {
            }

            @Override
            public void onAvatarClick(String username) {
            }
        });

        tvCountDown.setOnCountDownListener(new SimpleCountDownTextView.OnCountDownListener() {
            @Override
            public void onFinish() {
                loadBetDetail(null, 0);
            }
        });

        llLotteryLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadBetDetail(null, -1);
            }
        });

        titleBar.rightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckUtil.startCusSvr(activity);
            }
        });
        titleBar.rightImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChatBetFlowDlg(activity, gameType, roomId).showAsDropDown(titleBar.rightImage2);
            }
        });

        llRefreshPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialogUtil.showProgressDlg(getActivity(), "刷新中");
                loadBetDetail(null, 0);
            }
        });
    }

    public void handleBubbleClick(EMMessage message) {
        if(status == 2) {
            showTipDlg("已封盘，停止下注");
        } else if(status == 3) {
            showTipDlg("已停售，停止下注");
        } else if(status == 1) {
            if(message.getType() == EMMessage.Type.TXT) {
                EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                BettingJson json = new Gson().fromJson(txtBody.getMessage(), BettingJson.class);
                if(!json.game_count.equals(curGameNum)) {
                    showTipDlg("只能跟投当前期");
                } else {
                    showFollowDlg(json);
                }
            }
        }
    }

    public void showTipDlg(String msg) {
        new AlertDialog.Builder(activity)
                .setTitle("提示")
                .setMessage(msg)
                .setNegativeButton("确定", null)
                .show();
    }

    /**
     * 跟投对话框
     */
    public void showFollowDlg(final BettingJson json) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.layout_follow_bet_info, null);
        TextView tvNickName = (TextView) layout.findViewById(R.id.tvFollowNickName);
        TextView tvGameNum = (TextView) layout.findViewById(R.id.tvFollowGameNum);
        TextView tvResultType = (TextView) layout.findViewById(R.id.tvFollowResultType);
        TextView tvBetPoint = (TextView) layout.findViewById(R.id.tvFollowBetPoint);

        tvNickName.setText(json.nick_name);
        tvGameNum.setText(json.game_count);
        tvResultType.setText(json.game_type);
        tvBetPoint.setText(json.point);

        new AlertDialog.Builder(activity).setTitle("确定跟投吗?")
                .setView(layout)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double point = Double.parseDouble(json.point);
                        if(point > userPoint) {
                            TextView text = new TextView(activity);
                            text.setText(Html.fromHtml(getString(R.string.tip_to_recharge)));
                            text.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(activity, RechargeActivity.class));
                                }
                            });

                            new AlertDialog.Builder(activity).setTitle("提示")
                                    .setView(text)
                                    .setNegativeButton("确定", null)
                                    .show();
                        } else {
                            betting(json.game_count, point, Integer.parseInt(json.bili_id));
                        }
                    }
                }).show();
    }

    @Override
    public void onDestroy() {
        isActive = false;
        super.onDestroy();
        tvCountDown.stopCountDown();
        EMClient.getInstance().chatManager().removeMessageListener(emMessageListener);

        if(openingTimer != null) {
            openingTimer.cancel();
            openingTimer = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (inputMenu.onBackPressed()) {
            exitRoom();
        }
    }

    public void sendJoinedMsg() {
        JoinRoomRequest req = new JoinRoomRequest();
        req.room_id = roomId+"";
        MySubcriber s = new MySubcriber(new HttpResultCallback() {
            @Override
            public void onStart() {
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Object o) {
            }
        });
        ApiInterface.sendJoinedMsg(req, s);
    }


    public void exitRoom() {
        ExitRoomRequest req = new ExitRoomRequest();
        req.room_id = roomId+"";
        HttpResultCallback<String> callback = new HttpResultCallback<String>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onCompleted() {
                exitRoomSucceed();
            }

            @Override
            public void onError(Throwable e) {
                T.showShort(e.getMessage());
            }

            @Override
            public void onNext(String s) {
            }
        };
        MySubcriber<String> s = new MySubcriber<String>(activity, callback, true, "正在退出房间");
        ApiInterface.exitRoom(req, s);
    }

    public void exitRoomSucceed() {
        super.onBackPressed();
        T.showShort("退出房间成功");
    }

    /**
     * 获取下注赔率数据
     */
    public void loadOddsData() {
        GameTypeDataRequest req = new GameTypeDataRequest();
        req.area_id = areaId+"";
        req.game_type = gameType+"";
        HttpResultCallback<GameTypeInfo> callback = new HttpResultCallback<GameTypeInfo>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                T.showShort(e.getMessage());
            }

            @Override
            public void onNext(GameTypeInfo gameTypeInfo) {
                oddsDlg = BettingOddsDlg.getInstance(gameTypeInfo);
                oddsDlg.setBetOddsCallback(ChatBetFragment.this);
                oddsDlg.areaId = areaId;
                oddsDlg.minPoint = minPoint;
                oddsDlg.maxPoint = maxPoint;
                oddsDlg.show(getChildFragmentManager(), "");
            }
        };
        MySubcriber s = new MySubcriber(activity, callback, true, "获取数据");
        ApiInterface.getGameTypeData(req, s);
    }

    /** 选择赔率完进行下注 */
    @Override
    public void onBet(GameOddsInfo oddsInfo, double betPoint) {
        if(betPoint<oddsInfo.min_point) {
            T.showShort("最低投注金额为"+oddsInfo.min_point);
            return;
        }
        if(betPoint>oddsInfo.max_point) {
            T.showShort("最高投注金额为"+oddsInfo.max_point);
            return;
        }
        loadBetDetail(oddsInfo, betPoint);
    }

    /**
     * 房间下注记录以及倒计时接口
     * -1 显示开奖记录  0 刷新开盘状态  0< 下注
     */
    public void loadBetDetail(final GameOddsInfo oddsInfo, final double betPoint) {
        if(!isActive) {
            ProgressDialogUtil.dismissProgressDlg();
            return;
        }

        RoomBetDetailRequest req = new RoomBetDetailRequest();
        req.room_id = roomId+"";
        req.game_type = gameType+"";
        HttpResultCallback<BetDetailInfo> callback = new HttpResultCallback<BetDetailInfo>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onCompleted() {
                ProgressDialogUtil.dismissProgressDlg();
            }

            @Override
            public void onError(Throwable e) {
                ProgressDialogUtil.dismissProgressDlg();
                T.showShort(e.getMessage());
            }

            @Override
            public void onNext(BetDetailInfo betDetailInfo) {
                if(betPoint > 0) {
                    switch (betDetailInfo.status) {
                        case 1:
                            betting(betDetailInfo.game_num, betPoint, oddsInfo.id);
                            break;
                        case 2:
                            T.showShort("封盘中，暂停下注");
                            break;
                        case 3:
                            T.showShort("停售，暂停下注");
                            break;
                    }
                } else if(betPoint == -1) {
                    new LotteryLogDialog(activity, betDetailInfo.open_time).showAsDropDown(llLotteryLog);
                }
                initBetResult(betDetailInfo);
            }
        };
        MySubcriber s = new MySubcriber(activity, callback, betPoint==-1, "");
        ApiInterface.getBetDetail(req, s);
    }

    public void initBetResult(BetDetailInfo data) {
        this.status = data.status;
        this.curGameNum = data.game_num;

        minPoint = data.per_min_point;
        maxPoint = data.per_max_point;

        llBetResult.setVisibility(View.VISIBLE);
        tvCurGameNum.setText(Html.fromHtml(getString(R.string.current_game_num, data.game_num)));
        tvCountDown.stopCountDown();
        if(data.status == 1) {
            //开始游戏结束倒计时
            tvCountDown.startCountDown(data.seconds);
            if(openingTimer != null) {
                openingTimer.cancel();
                openingTimer = null;
            }
        } else {
            if (data.status == 2) {
                tvCountDown.setText("封盘中");
                //开始游戏开始倒计时
                openingTimer = new CountDownTimer(data.seconds * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        //封盘倒计时结束，开盘
                        loadBetDetail(null, 0);
                    }
                };
                openingTimer.start();
            } else if (data.status == 3) {
                tvCountDown.setText("停售中");
            }
        }

        userPoint = data.point;

        tvMyPoint.setText(data.point+"元宝");
        if(data.first_result != null) {
            tvPreGameNum.setText(Html.fromHtml(getString(R.string.pre_game_num, data.first_result.game_num+"")));
            tvBetFormula.setText(data.first_result.game_result_desc, data.first_result.color);
            tvBetResultType.setText("("+data.first_result.result_type+")");
        }
    }

    /**
     * 下注
     */
    public void betting(String gameNum, final double point, final int oddsId) {
        BettingRequest req = new BettingRequest();
        req.room_id = roomId+"";
        req.area_id = areaId+"";
        req.choice_no = gameNum;
        req.point = point+"";
        req.bili_id = oddsId+"";
        HttpResultCallback<String> callback = new HttpResultCallback<String>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onCompleted() {
                userPoint = Arith.sub(userPoint, point);
                tvMyPoint.setText(userPoint+"元宝");
                T.showShort("投注成功");
//                if(oddsDlg != null)
//                    oddsDlg.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                T.showShort(e.getMessage());
            }

            @Override
            public void onNext(String s) {
            }
        };
        MySubcriber s = new MySubcriber(activity, callback, true, "下注中");
        ApiInterface.betting(req, s);
    }
}
