/* 百度地图API V2 模块
 * 此模块必须配套使用baidumap_offline_v2_20160822.js对
 * 获取模块的方法：
 * http://api0.map.bdimg.com/getmodules?v=2.0&mod=模块1,模块2
 * 模块名称就是文件名
 * www.xiaoguo123.com 整理
 */
 _jsload2&&_jsload2('navictrl', 'x.extend(jb.prototype,{uf:function(){this.C&&this.Ce(this.C)},initialize:function(a){Sb.prototype.initialize.call(this,a);this.br();this.Iv();this.za();this.ca={};!G()?this.aQ():this.$P();this.Jv(a.fa());this.bQ(a);this.k.tW&&this.i_();return this.B},br:function(){this.Qi||(this.Qi=o,this.VS=19,this.no=-1,this.Mv=6,this.Xj=1,this.cl=this.bo=-1,this.Kv=this.dJ=this.Kf=p,this.uz="1100")},bQ:function(a){var b=this;a.addEventListener("zoomend",function(){b.C&&(b.Jv(b.C.fa()),!b.oI&&b.hr&&(b.zf=setTimeout(function(){b.oA()}, 1E3)))});a.addEventListener("mousewheel",function(){b.C&&(b.C.K.Po&&b.hr)&&(b.zf&&(clearTimeout(b.zf),b.zf=p),b.bJ())});a.addEventListener("load",function(){b.C&&b.Jv(b.C.fa())});a.addEventListener("maptypechange",function(){b.C&&b.fe()});a.addEventListener("zoomspanchange",function(){b.C&&b.fe()})},fe:function(){this.Iv();this.jn(this.k.type);this.C&&this.Jv(this.C.fa())},Iv:function(){var a=this.C.oa();this.Qh=this.C.K.fc;this.Cf=this.C.K.Zb;this.hr=a==Oa||this.Qh!=a.Xo()||this.Cf!=a.qm()?q:o;this.B&& (this.B.style.width=this.vH(0).width+"px");this.k.BF||(this.hr=q);this.no=this.Cf-this.Qh+1;this.bo=this.Xj+(this.no-1)*this.Mv},za:function(){Sb.prototype.za.call(this);var a=6==x.ea.la?" BMap_ie6":"",b=" BMap_stdMpType"+this.k.type,c=this.B;x.D.Ta(c,"BMap_stdMpCtrl");x.D.Ta(c,a);x.D.Ta(c,b);c.innerHTML=this.Rq(this.k.type);this.vB(x.ea.opera?"pointer":F.Wb);this.kd=x.$(c.children[0]);a=this.Kf=x.$(c.children[1]);this.Dh=x.$(a.children[0]);this.ag=x.$(a.children[1]);this.dJ=x.$(a.children[2]);this.Kv= x.$(a.children[2].children[0]);this.Lv=x.$(a.children[2].children[1]);this.cG=this.B.children[1];this.vD=this.B.children[2];this.jn(this.k.type)},cU:function(){var a=this.vH(this.k.type),b=a.width,c=a.height,d=a.su,a=a.Xt,e=(this.VS-this.no)*this.Mv;0==this.k.type&&(c=0<=c-e?c-e:0,d=0<=d-e?d-e:0,a=0<=a-e?a-e:0);this.B.style.width=b+"px";this.B.style.height=c+"px";this.Kf.style.height=d+"px";this.Kf.style.width=b+"px";this.ag.style.top=G()?"44px":d-21+"px";this.dJ.style.height=a+"px";this.Kv.style.height= a+"px";this.Kf.children[0].style.left=3==this.k.type?this.Kf.children[1].style.left="0":this.Kf.children[1].style.left=""},vH:function(a){var b=62;if(!this.k.BF||this.C.oa()==Oa)b=37;return[{width:b,height:204,su:159,Sy:37,Xt:120,Ey:120},{width:37,height:97,su:42,Sy:37,Xt:0,Ey:0},{width:37,height:57,su:0,Sy:0,Xt:0,Ey:0},{width:22,height:42,su:42,Sy:18,Xt:0,Ey:0},{width:35,height:71,Sy:35,su:71,Xt:0,Ey:0}][a]},Rq:function(){var a=[];a.push(this.$z());a.push(this.aA());a.push(this.HR());return a.join("")}, $z:ca(\'<div class="BMap_stdMpPan"><div class="BMap_button BMap_panN" title="\\u5411\\u4e0a\\u5e73\\u79fb"></div><div class="BMap_button BMap_panW" title="\\u5411\\u5de6\\u5e73\\u79fb"></div><div class="BMap_button BMap_panE" title="\\u5411\\u53f3\\u5e73\\u79fb"></div><div class="BMap_button BMap_panS" title="\\u5411\\u4e0b\\u5e73\\u79fb"></div><div class="BMap_stdMpPanBg BMap_smcbg"></div></div>\'),aA:function(){return G()?[\'<div class="BMap_stdMpZoom" style="width: 35px; height: 71px; background: rgba(255,255,255,.8); -webkit-box-shadow: 1px 1px 2px rgba(0,0,0,.4); border-radius: 3px;"><div class="BMap_button BMap_stdMpZoomIn" title="\\u653e\\u5927\\u4e00\\u7ea7" style="position: initial; background: initial; border-radius: initial; box-shadow: initial; width: 35px; height: 35px;">\', \'<div class="BMap_smcbg" style="position: initial; width: 35px; height: 35px; background-image: url(\'+z.ma+\'../../images/navigation-control/mobile/plus-28x29.png); background-size: 14px 15px; background-position: center; background-repeat: no-repeat;">\',\'</div></div><div class="BMap_button BMap_stdMpZoomOut" title="\\u7f29\\u5c0f\\u4e00\\u7ea7" style="position: initial; background: initial; border-radius: initial; box-shadow: initial; width: 35px; height: 35px; border-top: 1px solid #ececec;">\',\'<div class="BMap_smcbg" style="position: initial; width: 35px; height: 35px; background-image: url(\'+ z.ma+\'images/navigation-control/mobile/minus-30x6.png); background-size: 15px 3px; background-position: center; background-repeat: no-repeat;">\',\'</div></div><div class="BMap_stdMpSlider"><div class="BMap_stdMpSliderBgTop"><div class="BMap_smcbg"></div></div><div class="BMap_stdMpSliderBgBot"></div><div class="BMap_stdMpSliderMask" title="\\u653e\\u7f6e\\u5230\\u6b64\\u7ea7\\u522b"></div><div class="BMap_stdMpSliderBar" title="\\u62d6\\u52a8\\u7f29\\u653e"><div class="BMap_smcbg"></div></div></div><div class="BMap_zlHolder"><div class="BMap_zlSt"><div class="BMap_smcbg"></div></div><div class="BMap_zlCity"><div class="BMap_smcbg"></div></div><div class="BMap_zlProv"><div class="BMap_smcbg"></div></div><div class="BMap_zlCountry"><div class="BMap_smcbg"></div></div></div></div>\'].join(""): \'<div class="BMap_stdMpZoom"><div class="BMap_button BMap_stdMpZoomIn" title="\\u653e\\u5927\\u4e00\\u7ea7"><div class="BMap_smcbg"></div></div><div class="BMap_button BMap_stdMpZoomOut" title="\\u7f29\\u5c0f\\u4e00\\u7ea7"><div class="BMap_smcbg"></div></div><div class="BMap_stdMpSlider"><div class="BMap_stdMpSliderBgTop"><div class="BMap_smcbg"></div></div><div class="BMap_stdMpSliderBgBot"></div><div class="BMap_stdMpSliderMask" title="\\u653e\\u7f6e\\u5230\\u6b64\\u7ea7\\u522b"></div><div class="BMap_stdMpSliderBar" title="\\u62d6\\u52a8\\u7f29\\u653e"><div class="BMap_smcbg"></div></div></div><div class="BMap_zlHolder"><div class="BMap_zlSt"><div class="BMap_smcbg"></div></div><div class="BMap_zlCity"><div class="BMap_smcbg"></div></div><div class="BMap_zlProv"><div class="BMap_smcbg"></div></div><div class="BMap_zlCountry"><div class="BMap_smcbg"></div></div></div></div>\'}, L0:ca(\'<div class="BMap_button BMap_stdMpZoomIn" title="\\u653e\\u5927\\u4e00\\u7ea7"><div class="BMap_smcbg"></div></div><div class="BMap_button BMap_stdMpZoomOut" title="\\u7f29\\u5c0f\\u4e00\\u7ea7"><div class="BMap_smcbg"></div></div>\'),J0:ca(\'<div class="BMap_stdMpSlider"><div class="BMap_stdMpSliderBgTop"><div class="BMap_smcbg"></div></div><div class="BMap_stdMpSliderBgBot"></div><div class="BMap_stdMpSliderMask" title="\\u653e\\u7f6e\\u5230\\u6b64\\u7ea7\\u522b"></div><div class="BMap_stdMpSliderBar" title="\\u62d6\\u52a8\\u7f29\\u653e"><div class="BMap_smcbg"></div></div></div>\'), K0:ca(\'<div class="BMap_zlHolder"><div class="BMap_zlSt"><div class="BMap_smcbg"></div></div><div class="BMap_zlCity"><div class="BMap_smcbg"></div></div><div class="BMap_zlProv"><div class="BMap_smcbg"></div></div><div class="BMap_zlCountry"><div class="BMap_smcbg"></div></div></div>\'),HR:function(){return G()?[\'<div class="BMap_stdMpGeolocation" style="position: initial; display: none; position: absolute; left: -\'+(this.C.width-46)+\'px; bottom: 0px;">\',\'<div class="BMap_geolocationContainer" style="position: initial; width: 38px; height: 38px; overflow: hidden; margin: 0px; margin-left: 10px; margin-bottom: 20px; box-sizing: border-box;">\', \'<div class="BMap_geolocationIconBackground" style="position: initial; width: 38px; height: 38px; background-image: url(\\\'\'+z.ma+"images/navigation-control/geolocation-control/mobile/background-76x76.png\'); background-size: 38px 38px; background-repeat: no-repeat; background-position: center;\\">",\'<div style="position: initial; padding-top: 9px; padding-left: 9px;">\',\'<div class="BMap_geolocationIcon" style="position: initial; width: 20px; height: 20px; cursor: pointer; background-image: url(\\\'\'+ z.ma+"images/navigation-control/geolocation-control/mobile/default-40x40.png\'); background-size: 20px 20px; background-repeat: no-repeat; background-position: center;\\"></div>","</div></div></div></div>"].join(""):[\'<div class="BMap_stdMpGeolocation" style="position: initial; display: none; margin-top: 43px; margin-left: 10px;"><div class="BMap_geolocationContainer" style="position: initial; width: 24px; height: 24px; overflow: hidden; margin: 0px; box-sizing: border-box;">\',\'<div class="BMap_geolocationIconBackground" style="width: 24px; height: 24px; background-image: url(\'+ z.ma+\'images/navigation-control/geolocation-control/pc/bg-20x20.png); background-size: 20px 20px; background-position: 3px 3px; background-repeat: no-repeat;">\',\'<div class="BMap_geolocationIcon" style="position: initial; width: 24px; height: 24px; cursor: pointer; background-image: url(\\\'\'+z.ma+"images/navigation-control/geolocation-control/pc/success-10x10.png\'); background-size: 10px 10px; background-repeat: no-repeat; background-position: center;\\"></div>","</div></div></div>"].join("")},TR:function(a){var b= this.TR;"undefined"===typeof b.ZJ&&(b.ZJ=me.B.getElementsByTagName("*"));for(var b=b.ZJ,c=p,d=b.length,e=p,c=0;c<d;++c)if(e=b[c],e.className.toString().match(a))return e;aa(Error(""+a+" Not found!"))},jn:function(a){this.k.type=Va(a)&&0<=a&&3>=a?a:0;G()&&(this.k.type=4);if(this.C&&this.B){var b=this.B;b.className=b.className.replace(/BMap_stdMpType[0-4]*/,"BMap_stdMpType"+this.k.type);this.cU();0!=a&&x.D.Rb(b.children[1].children[2],"hvr");this.qc(this.k.anchor)}},fp:function(){return this.k.type}, aQ:function(){function a(){if(0!=(c.C.Sa&128)){var e=x.$(g);c.cl=parseInt(e.style.top);x.D.Rb(g,"h");c.C.Sa&=-129;d&&(g&&g.releaseCapture)&&g.releaseCapture();x.ea.opera||c.vB(F.Wb);c.C.Fg(c.Cf+1-Math.round(parseFloat(c.cl-c.Xj)/parseFloat(c.bo-c.Xj)*(c.no-1)+1));x.cd(document,"mousemove",b);x.cd(document,"mouseup",a)}}function b(a){0!=(c.C.Sa&128)&&(a=window.event||a,a=c.cl+(a.pageY||a.clientY)-c.ca.Ek,a<c.Xj?a=c.Xj:a>c.bo&&(a=c.bo),g.style.top=a+"px",c.Lv.style.top=a+"px",c.Lv.style.height=parseInt(c.Kv.style.height)- a+4+"px")}var c=this,d=c.B;x.M(this.Kf,"mouseover",function(){c.hr&&(c.oI=o,c.zf&&(clearTimeout(c.zf),c.zf=p),c.bJ())});x.M(this.Kf,"mouseout",function(){c.hr&&(c.zf&&clearTimeout(c.zf),c.oI=q,c.zf=setTimeout(function(){c.oA();c.zf=p},1E3))});x.M(d.children[0],"mouseover",function(){c.oA(o)});x.M(d.children[0].children[0],"click",function(){c.yl(0,Math.round(c.C.height/3))});x.M(d.children[0].children[1],"click",function(){c.yl(Math.round(c.C.width/3),0)});x.M(d.children[0].children[2],"click",function(){c.yl(-Math.round(c.C.width/ 3),0)});x.M(d.children[0].children[3],"click",function(){c.yl(0,-Math.round(c.C.height/3))});x.M(d.children[0].children[0],"mouseover",function(){c.kd.style.backgroundPosition="0 -44px"});x.M(d.children[0].children[1],"mouseover",function(){c.kd.style.backgroundPosition="0 -176px"});x.M(d.children[0].children[2],"mouseover",function(){c.kd.style.backgroundPosition="0 -88px"});x.M(d.children[0].children[3],"mouseover",function(){c.kd.style.backgroundPosition="0 -132px"});x.M(c.kd,"mouseout",function(){c.kd.style.backgroundPosition= "0 0"});var e=d.children[1].children;x.M(e[0],"click",function(){c.AJ()});x.M(e[1],"click",function(){c.BJ()});for(var f=0;5>f;f++)x.M(d.children[0].children[f],"mouseup",function(a){0==(c.C.Sa&128)&&0==(c.C.Sa&8)&&ma(a)}),x.M(d.children[0].children[f],"contextmenu",function(a){Bb(a)}),x.M(d.children[0].children[f],"click",function(a){Bb(a)});x.M(e[0],"mouseup",function(a){0==(c.C.Sa&128)&&0==(c.C.Sa&8)&&ma(a)});x.M(e[1],"mouseup",function(a){0==(c.C.Sa&128)&&0==(c.C.Sa&8)&&ma(a)});x.M(e[0],"contextmenu", function(a){Bb(a)});x.M(e[1],"contextmenu",function(a){Bb(a)});f=d.children[1].children[2].children[2];x.M(f,"mouseup",function(a){0==(c.C.Sa&128)&&0==(c.C.Sa&8)&&ma(a)});x.M(e[0],"click",function(a){ma(a)});x.M(e[1],"click",function(a){ma(a)});x.M(e[0],"mouseover",function(){e[0].style.backgroundPosition="0 -243px"});x.M(e[0],"mouseout",function(){0==(c.C.Sa&128)&&(e[0].style.backgroundPosition="0 -221px")});x.M(e[1],"mouseover",function(){e[1].style.backgroundPosition="0 -287px"});x.M(e[1],"mouseout", function(){0==(c.C.Sa&128)&&(e[1].style.backgroundPosition="0 -265px")});x.M(f,"click",function(a){ma(a)});var g=d.children[1].children[2].children[3];x.M(g,"mouseup",function(a){2==a.button&&ma(a)});x.M(g,"contextmenu",function(a){Bb(a)});x.M(this.Kf.children[3].children[0],"click",function(){c.C.Fg(17)});x.M(this.Kf.children[3].children[1],"click",function(){c.C.Fg(12)});x.M(this.Kf.children[3].children[2],"click",function(){c.C.Fg(8)});x.M(this.Kf.children[3].children[3],"click",function(){c.C.Fg(4)}); x.M(f,"mousedown",function(a){var a=window.event||a,b=0,b=c.Cf+1-Math.round(c.no*parseFloat((a.layerY||a.offsetY||0)/(c.no*c.Mv)));c.C.Fg(b)});x.M(g,"mouseover",function(){x.D.Ta(g,"h")});x.M(g,"mouseout",function(){0==(c.C.Sa&128)&&x.D.Rb(g,"h")});x.M(g,"mousedown",function(d){d=window.event||d;if(2!=d.button&&!(x.ea.la&&1!=d.button))return g.setCapture&&g.setCapture(),c.C.Sa|=128,c.ca.Ek=d.pageY||d.clientY||0,x.ea.opera||c.vB(F.Ed),x.M(document,"mousemove",b),x.M(document,"mouseup",a),ma(d),Bb(d)})}, $P:function(){var a=this,b=a.B.children[1].children;ha.SK(b[0]);ha.SK(b[1]);ha.M(b[0],"tap",function(){a.AJ()});ha.M(b[1],"tap",function(){a.BJ()})},vB:function(a){this.B.children[1].children[2].children[3].style.cursor=a},yl:function(a,b){this.C.yg(a,b)},AJ:function(){this.C.VF()},BJ:function(){this.C.WF()},Jv:function(a){this.B&&0==this.fp()&&(a=(this.Cf-a)*this.Mv+this.Xj,this.cl=a>this.bo?this.bo:a<this.Xj?this.Xj:a,this.B.children[1].children[2].children[3].style.top=this.cl+"px",this.Lv.style.top= this.cl+"px",this.Lv.style.height=parseInt(this.Kv.style.height)-this.cl+4+"px")},oA:function(a){0==this.k.type&&x.D.Rb(this.B.children[1].children[3],"hvr");a&&this.zf&&(clearTimeout(this.zf),this.zf=p)},bJ:function(){0==this.k.type&&this.k.BF&&x.D.Ta(this.B.children[1].children[3],"hvr")},show:function(){Sb.prototype.show.call(this);if(8>x.ea.la){var a=this;setTimeout(function(){a.jn(a.k.type)},1)}},dY:function(){var a=this,b={};G()?(b.pj=a.vD.children[0],b.So=b.pj.children[0].children[0].children[0]): (b.pj=a.vD.children[0],b.wD=b.pj.children[0],b.So=b.wD.children[0]);G()?a.C.addEventListener("resize",function(){b.pj.parentNode.style.left=(-(a.C.width-46)).toString()+"px"}):3===a.k.type&&(a.vD.style.marginLeft="-2px");b.At={"default":z.ma+"images/navigation-control/geolocation-control/pc/success-10x10.png",loading:z.ma+"images/navigation-control/geolocation-control/pc/loading-30x30.gif",success:z.ma+"images/navigation-control/geolocation-control/pc/success-10x10.png",fail:z.ma+"images/navigation-control/geolocation-control/pc/fail-10x10.png"}; b.ut={"default":z.ma+"images/navigation-control/geolocation-control/mobile/default-40x40.png",loading:z.ma+"images/navigation-control/geolocation-control/mobile/loading-40x40.png",success:z.ma+"images/navigation-control/geolocation-control/mobile/success-40x40.png",fail:z.ma+"images/navigation-control/geolocation-control/mobile/fail-40x40.png"};var c;a.C.addEventListener("moveend",function(){if(c){var d=a.C.Ja();d.lng===c.lng&&d.lat===c.lat?G()?b.Tb(b.ut.success):b.Tb(b.At.success):G()?b.Tb(b.ut["default"]): b.Tb(b.At["default"])}});x.M(b.So,"click",function(){G()?b.Tb(b.ut.loading):b.Tb(b.At.loading);(new Geolocation({timeout:1E4})).getCurrentPosition(function(a){G()?b.Tb(b.ut.success):b.Tb(b.At.success);var a=new H(a.longitude,a.latitude),e=new T(a,{icon:new nc(z.ma+"images/navigation-control/geolocation-control/point/position-icon-14x14.png",new L(14,14))});map.Ia(e);map.Cd(a,15);c=a},function(){G()?b.Tb(b.ut.fail):b.Tb(b.At.fail)})});b.Tb=function(a){b.So.style.backgroundImage="url(\'"+a+"\')"};this.eL= b;this.PW=o},i_:function(){G()||(this.PW||this.dY(),G()||(this.cG.style.top="70px"),this.eL.pj.parentNode.style.display="block")},M2:function(){G()||(this.cG.style.top="45px");this.eL.pj.parentNode.style.display="none"}});jb.prototype.setType=jb.prototype.jn; ');
