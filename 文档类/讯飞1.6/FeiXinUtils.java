package com.thinkive.framework.util;

import com.iflytek.cloud.speech.*;
import com.thinkive.base.config.Configuration;
import com.thinkive.base.jdbc.DataRow;
import com.thinkive.weixin.base.service.WeiXinRedisClient;

import org.apache.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * 描述：飞讯语音合成
 * @author 汤成
 * @created 2016年7月8日 下午2:45:22
 * @since
 */
public class FeiXinUtils 
{
    private static Logger    logger    = Logger.getLogger(FeiXinUtils.class);
    private static Object  error_n =null;
    private static int progress_n =-1;
    private static boolean f = false;
    private static String url;
    static int i =1;
    static WeiXinRedisClient client =new WeiXinRedisClient();
    //private static String PATH = "";//文件路径
  
    
	public static DataRow synthesizeToUri(String content, final String path)
	{
	    File file = new File(path+".wav");
	    
	        //PATH = path;
	    final DataRow data = new DataRow();
		String appid = Configuration.getString("system.appid", "577e2d72");
		String voiceName = Configuration.getString("system.voiceName", "xiaoyan");
		//      param.append( ","+SpeechConstant.LIB_NAME_32+"=myMscName" );
		SpeechUtility.createUtility("appid=" + appid);
		//1.创建SpeechSynthesizer对象  
		SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer();
		//2.合成参数设置，详见《iFlytek MSC Reference Manual》SpeechSynthesizer 类  
		
            mTts.setParameter(SpeechConstant.VOICE_NAME, voiceName);//设置发音人  
            mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速，范围0~100  
            mTts.setParameter(SpeechConstant.PITCH, "50");//设置语调，范围0~100  
            mTts.setParameter(SpeechConstant.VOLUME, "50");//设置音量，范围0~100  
            mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, path);
            // 设置播放器音频流类型  
            mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
            
            
                
                //3.开始合成  
                //设置合成音频保存位置（可自定义保存位置），默认保存在“iflytek.pcm”  
            mTts.synthesizeToUri(content, path+".pcm",  new SynthesizeToUriListener()
            {
                //progress为合成进度0~100   
                public void onBufferProgress(int progress)
                {

                    progress_n = progress;
                    logger.info(progress_n);
                   /* client.set("yuying@"+path,progress+"");
                    client.expire("yuying@"+path, 60*30);//设置过期时间30分钟
*/                  if(progress==100){
                                try {
                                     String str = ConvertAudio.convertAudioFiles(path+".pcm", path+".wav");
                                     logger.info("ConvertAudio.convertAudioFiles正常");
                                     if(str.equals("OK"));{
                                        f = true;
                                     }
                                }catch (Exception e)  {
                                        f = false;
                                        logger.info(e);
                                }
                    }
                //会话合成完成回调接口  
                //uri为合成保存地址，error为错误信息，为null时表示合成会话成功  
                }
                
                public void onSynthesizeCompleted(String uri, SpeechError error)
                {
                    logger.error("合成路径:"+uri);
                    logger.error("合成情况:"+error);
                    error_n = error;
                    url = uri;
                   /* client.set("yuying_error@"+path,error.toString());
                    client.expire("yuying_error@"+path, 60*30);*/
                     
                }
            });
            int m=0;
           
            while(true){
                m++;
                try
                {
                    Thread.sleep(300);//延迟加载语音合成返回值
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                    return data;
                   
                };
                if(f&&file.exists()){
                    //合成成功
                    logger.info(f);
                    return data;
                }else if((m==1&&progress_n==100&&error_n!=null)||(m==10&&progress_n==-1&&error_n!=null)){
                      data.set("error","合成失败");
                      return data;
                }             
            }
           
        }

            public  static  void main(String... args) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                String separator = File.separator;
                String path = "D:";
                String xdpath = separator+"upload"+separator + dateFormat.format(new Date()) + separator + System.currentTimeMillis();
                String filePath = path+ xdpath;
                DataRow data =  FeiXinUtils.synthesizeToUri(" 无形的洪荒之力，把应有的责任心冲打的溃不成军，霓虹灯飘渺的光影下，一颗心越来越虚浮。忍耐的底线越来越高，包容的尺度越来越小，关怀的甜点越来越少。越来越多的与时俱进的学会了自私自利的享受，把自己高高在上的放在心中无可替代的位置。爱，成了一个人的独角戏，不再分享与共。\n" +
                        "\n" +
                        "　　 庄严神圣的大红色结婚证书，渐渐的褪去了它的终身制和不可侵犯的尊严，变成了可以随意调换的商品，不再用“从一而终”的心态去呵护它，守卫它。闪婚，闪离，踏着快节奏的生活旋律，走着快节奏的步伐，新生代的名词在一闪过后，铺天盖地的流行起来。\n" +
                        "\n" +
                        "　　 一只刚出炉的面包，在适宜的温度下有三天的寿命，若放在高温潮湿的地方，能隔夜不发霉，就很不错了。采摘下一只健康的苹果，在妥善的保存下要半年才能脱水干瘪或者是腐烂。若放在桌上置若罔闻，顶多采摘下来一个月，外观就让人不忍直视。它们寿命的长短，不在乎是否劣质，而是没有正确的，稳妥的，用心的去呵护。\n" +
                        "\n" +
                        "　　 爱情也一样，需要妥善的保管才能让它的生命力发挥到极致。携手走入婚姻围城的俩个人，不管初遇时的心动多么激烈，若是不好好的用心供养爱情的玫瑰花园，保鲜期会提前，会在凋零之后发霉腐烂。不管贫富与否，爱情里从来不缺少浪漫，缺少的是把激情正确的转化为亲情的方向，让两颗心，不仅仅是你装着我，我装着你，还默契的装着我们对未来的打算和珍藏一路共有的记忆。\n" +
                        "\n" +
                        "　　 婚姻，不是用来捆绑感情的，而是责任的升级。有感情的人不一定非要结婚，但结了婚就一定要负起责任。组建起这个家庭的男人和女人，不再是单纯的个体，而是既能自由活动又能互相约束的结合体。同在一个屋檐下，共同打造一个家，而这个“家”质量的好坏，能不能长久的使用，和遭遇风雨时能不能得到妥善的维护，是要两个人齐心协力去付出的。\n" +
                        "\n" +
                        "　　 好的婚姻，都是用心经营出来的。一份呵护打造一份和谐，一份容忍收获一份安稳，一份付出得到一份明媚。经营的阳光和绚，鸟语花香的家，不管涉入尘世的心有多疲惫，迈进家门的那一刻，轻松宜人的氛围都会把心灵上的尘埃轻轻的拂去。一句暖心的话，一杯喜欢的茶，在日复一日的简单中，让纯真的感情愈发的浓厚。\n" +
                        "\n" +
                        "　　 温暖心灵的不是暖气，拂去烦躁的不是空调，而是一份懂得与体贴。能够拴住一颗心的不是容貌与身材，也不是地位与身价，而是另一颗心的内涵。两情相悦，四壁透风的草房，也会被内在的幸福填满。家是心灵的港湾，是灵魂栖息的地方。不需要奢华，却一定要舒适。\n" +
                        "\n" +
                        "　　 婚姻的灵魂是感情，一个没有好好维系感情的婚姻就是一个空壳，无论装潢多么的奢侈华丽，都会在风雨中不堪一击。每天，细腻的感受爱人的一颦一笑，适时的调节自己心情，转换自己的思想。用心的接受身边人的悲与欢，对与错的同时，也坦诚的对身边人敞开自己心扉，把自己的苦与乐，阴与晴用恰当的方式倾诉与爱人。\n" +
                        "\n" +
                        "　　 交流，是让爱情长久不可或缺的方式之一。有些顾虑，说出来，心结会轻轻的打开。有些矛盾，说到明处，干戈就会化为玉帛。有些苦衷，说出来，紧蹙的眉头就会云淡风轻。爱人也是人，不会未卜先知，也不会能掐会算，有些话，不说，就失去了让TA懂得的机会。即便不懂，TA还可以问，你还可以解释。\n" +
                        "\n" +
                        "　　 夫妻关系和任何一种关系都不一样。手足之情，朋友之情，都是真挚的，甚至我们可以拿命去换，可真的某天利益当头，还是会掂量下轻重，考虑一下是否可行。夫妻的利益不分孰重孰轻，一荣俱荣，一损俱损，没有彼此之分，没有胜败之说，确切的说，彼此之间的任何事态，都没有什么利益或是损失可以是单对某一方的。\n" +
                        "\n" +
                        "　　 这样的两个人，没有理由不相互敞开心扉，掏出肺腑。在为事业打拼的空闲时，在烟熏火燎的罅隙里，背靠着背，对着一窗新月，把柔软的心敞开，听听爱人的心，也让爱人听听自己的心。用自己温柔的体贴缝补爱人闯荡在外留下的伤口，而自己在人际中的憋屈也让爱人用TA的细腻温情融化。\n" +
                        "\n" +
                        "　　 最好的婚姻，不是郎才女貌，门当户对，而是无才无貌，门不当户不对的两个人，彼此在乎，彼此珍惜，彼此忍让，彼此奉献，因为心中真挚的爱，粗茶淡饭才会在唇齿间留香，粗布衣衫才能穿出眼中旖旎的风景。\n" +
                        "\n" +
                        "　　 能够走进婚姻的殿堂，无外乎是一见倾心或是日久生情。只是那个当初怦然心动的人在柴米油盐中黯然了色彩。当青蛙王子天天为生活奔波的时候，他的帅气和体贴难免打些折扣；当白雪公主时时为生活操碎了心的时候，她的贤淑与温柔难免遗失些许。不要抱怨婚前与婚后的TA判若两人，能够改变对方的因素大部分是自己造成的。\n" +
                        "\n" +
                        "　　 作为一个男人若能顶天立地的为爱人撑起一片晴空，用关怀和呵护让她无忧无虑如少女般生活在温馨的摇篮里，被爱情滋润的她会更加可爱，比婚前有过之而无不及。没有哪个女人愿意做怨妇，心情舒畅的女人会把锅碗瓢盆的协奏曲弹奏的很美妙，虽比不了天籁之音，却也能让人流连忘返。\n" +
                        "\n" +
                        "　　 做为一个女人若能用一个贤妻良母的温婉为爱人解去后顾之忧，用体贴和善解人意让他能够放手自己的事业，被爱情浇灌的他会更加成熟稳重，用全身心去为家打拼。没有哪个男人愿意把家庭当作战场，温暖舒适的小窝会让他乐此不彼的为家人系上围裙，用一粥一饭诱惑爱人的味蕾，温暖爱人的心扉。\n" +
                        "\n" +
                        "　　 生活的快节奏，社会的高强度压力，免不了会有心理上的压抑需要用发泄来缓解。而发泄的方式有很多种，有人酗酒，有人玩命工作，有人大发雷霆，喋喋不休的抱怨，只有少数明智的人，才会对着爱人，娓娓道出自己的感觉。甘苦与共的另一半，也有自己的独立的思想，有时候TA会无法接受你的缓解的方式。勺子碰到锅沿时，恰当的退一步，给枕边人一份包容，一份体谅和一分钟的解释。\n" +
                        "\n" +
                        "　　 细腻的爱情会擦去在尘世中跋涉而蒙尘的心，会修复在风吹雨打中道道伤痕，会营养在外应酬疲惫而枯燥的心。好的爱情是婚姻的润滑剂，在不断的注入下，婚姻会永远的和谐美好。“百年修得同船渡，千年修得共枕眠。”虽然我不相信前世来生的说法，相信人只是个一次性的肉体，用完之后，就会回归自然的垃圾场。但在千千万万的人之中，能够遇见真的不容易，应该用生命珍惜这份来之不易的缘分。\n" +
                        "\n" +
                        "　　 两个人在一起，同在一个屋檐下，并驾齐驱，没有高低贵贱之分，没有聪明笨傻之分。在日久的相濡以沫里，习惯慢慢融合，性格慢慢互补，爱的激情慢慢升华成血脉相连的亲情。爱与情，不仅仅是左手牵右手的浪漫，更是息息相通的心与肺；不仅仅是心甘情愿的做另一半的拐杖，更愿意做TA的眼，TA的嘴，TA的一切的一切……", filePath);
            }
	    }
		 
