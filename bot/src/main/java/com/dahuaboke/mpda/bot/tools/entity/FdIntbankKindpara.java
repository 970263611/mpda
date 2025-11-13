package com.dahuaboke.mpda.bot.tools.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;


@Table(name = "fd_intbank_kindpara")
public class FdIntbankKindpara implements Serializable {
    /**
     * 基金产品编码	||基金产品编码
     */
    @Id
    @Column(name = "fund_prodt_no")
    private String fundProdtNo;

    /**
     * 登记机构号||TA登记机构
     */
    @Column(name = "enroll_inst_no")
    private String enrollInstNo;

    /**
     * 所属机构号||产品所属机构
     */
    @Column(name = "belong_inst_no")
    private String belongInstNo;

    /**
     * 基金状态代码||0-可申购赎回，1-发行4-停止申购赎回5-停止申购，6-停止赎回8-基金终止，9-基金封闭
     */
    @Column(name = "fund_status_code")
    private String fundStatusCode;

    /**
     * 产品名称||产品名称
     */
    @Column(name = "prodt_name")
    private String prodtName;

    /**
     * 募集起始日期|募集起始日期
     */
    @Column(name = "rais_bgn_date")
    private String raisBgnDate;

    /**
     * 募集结束日期|募集结束日期
     */
    @Column(name = "collect_end_dt")
    private String collectEndDt;

    /**
     * 募集天数|募集天数
     */
    @Column(name = "collect_daynum")
    private Long collectDaynum;

    /**
     * 收费方式||0-前端收费1-后端收费
     */
    @Column(name = "ctfee_mode_cd")
    private String ctfeeModeCd;

    /**
     * 基金类型||0-货币型基金1-股票型基金2-债券型基金3-混合型基金4-保本型基金	5-指数型6-短债型7-QDII
     */
    @Column(name = "fund_type")
    private String fundType;

    /**
     * 基金二级分类||01-普通股票型、02-被动指数型、03-增强指数型、04-偏股混合型、05-平衡混合型、06-偏债混合型、07-灵活配置型、08-中长期纯债型、09-短期纯债型、10-混合债券型一级、11-混合债券型二级、12-被动指数债券型、13-增强指数债券型、14-传统货币型、15-短期理财型、16-浮动净值型货币、17-股票多空型、18-商品型、19-QDII股票型、20-QDII债券型、21-QDII混合型、22-QDII另类型、99-其他
     */
    @Column(name = "fund_lvl2_cls_cd")
    private String fundLvl2ClsCd;

    /**
     * 理财产品风险等级代码||1-低2-中低3-中4-中高5-高
     */
    @Column(name = "chrem_prodt_risk_lvl_code")
    private String chremProdtRiskLvlCode;

    /**
     * 默认分红方式||0-红利转股1-现金分红
     */
    @Column(name = "defau_divid_mode")
    private String defauDividMode;

    /**
     * 机构最低认购限额||机构最低认购限额
     */
    @Column(name = "inst_lowest_subscri_lmt_amt")
    private BigDecimal instLowestSubscriLmtAmt;

    /**
     * 机构追加最低认购限额||机构追加最低认购限额
     */
    @Column(name = "instaddto_lowest_subscri_lmamt")
    private BigDecimal instaddtoLowestSubscriLmamt;

    /**
     * 机构最高认购限额||机构最高认购限额
     */
    @Column(name = "inst_highest_subscri_lmt_amt")
    private BigDecimal instHighestSubscriLmtAmt;

    /**
     * 机构最低申购限额||机构最低申购限额
     */
    @Column(name = "inst_lowest_aplypchs_lmt_amt")
    private BigDecimal instLowestAplypchsLmtAmt;

    /**
     * 机构追加最低申购限额||机构追加最低申购限额
     */
    @Column(name = "instaddto_lowe_aplypchs_lmamt")
    private BigDecimal instaddtoLoweAplypchsLmamt;

    /**
     * 机构单笔最高申购限额||机构单笔最高申购限额
     */
    @Column(name = "inst_per_highest_aplyp_lmt_amt")
    private BigDecimal instPerHighestAplypLmtAmt;

    /**
     * 机构最高申购限额||机构最高申购限额
     */
    @Column(name = "inst_highest_aplypchs_lmt_amt")
    private BigDecimal instHighestAplypchsLmtAmt;

    /**
     * 机构最低赎回限额||机构最低赎回限额
     */
    @Column(name = "inst_lowest_redem_lmt_amt")
    private BigDecimal instLowestRedemLmtAmt;

    /**
     * 机构单笔最高赎回限额||机构单笔最高赎回限额
     */
    @Column(name = "inst_per_highest_redem_lmt_amt")
    private BigDecimal instPerHighestRedemLmtAmt;

    /**
     * 机构最高赎回限额||机构最高赎回限额
     */
    @Column(name = "inst_highest_redem_lmt_amt")
    private BigDecimal instHighestRedemLmtAmt;

    /**
     * 机构最低持有限额||机构最低持有限额
     */
    @Column(name = "inst_lowest_hold_have_lmt_amt")
    private BigDecimal instLowestHoldHaveLmtAmt;

    /**
     * 基金转换下限金额||基金转换下限金额
     */
    @Column(name = "fund_tranfm_low_lmt_amt")
    private BigDecimal fundTranfmLowLmtAmt;

    /**
     * 定期定额申购上限金额||定期定额申购上限金额
     */
    @Column(name = "fix_quota_aplypchs_up_lmt_amt")
    private BigDecimal fixQuotaAplypchsUpLmtAmt;

    /**
     * 定期定额申购下限金额||定期定额申购下限金额
     */
    @Column(name = "fix_quota_aplypchs_low_lmt_amt")
    private BigDecimal fixQuotaAplypchsLowLmtAmt;

    /**
     * 拼音名称||拼音名称全拼
     */
    @Column(name = "en_py_name")
    private String enPyName;

    /**
     * 拼音简称||产品拼音首字母
     */
    @Column(name = "en_py_sname")
    private String enPySname;

    /**
     * 产品名称||基金产品全称
     */
    @Column(name = "prodt_aname")
    private String prodtAname;

    /**
     * 基金托管银行||基金托管银行
     */
    @Column(name = "fund_trustee_bank_name")
    private String fundTrusteeBankName;

    /**
     * 短期理财标志||0-否1-是
     */
    @Column(name = "chrem_flag")
    private String chremFlag;

    /**
     * 合同类型代码||0-纸质；1-电子
     */
    @Column(name = "contract_type")
    private String contractType;

    /**
     * 合同名称||业务合同名称
     */
    @Column(name = "busi_contract_name")
    private String busiContractName;

    /**
     * 电子确认方代码||0-基金公司确认1-中登确认
     */
    @Column(name = "e_conf_par_code")
    private String eConfParCode;

    /**
     * 是否是lof基金标志||0-非lof基金1-lof基金
     */
    @Column(name = "lof_fund_flag")
    private String lofFundFlag;

    /**
     * 某年持有基金标志|0-否，1-是
     */
    @Column(name = "partc_yr_hold_hv_fund_flag")
    private String partcYrHoldHvFundFlag;

    /**
     * 是否被退出标志||0-正常1-已退出
     */
    @Column(name = "byquit_flag")
    private String byquitFlag;

    /**
     * 是否关联方管理产品标志|0-否，1-是
     */
    @Column(name = "rel_pty_manage_prodt_flag")
    private String relPtyManageProdtFlag;

    /**
     * 是否集合理财产品标志||0-否1-是
     */
    @Column(name = "sets_chmprdt_flag")
    private String setsChmprdtFlag;

    /**
     * 是否结构化产品标志|0-否，1-是
     */
    @Column(name = "structur_prodt_flag")
    private String structurProdtFlag;

    /**
     * 是否开办定期定额业务||0–开办1–不开办
     */
    @Column(name = "open_fix_quota_busi_flag")
    private String openFixQuotaBusiFlag;

    /**
     * 是否控制额度||0-允许；1-不允许
     */
    @Column(name = "ctrl_limit_flag")
    private String ctrlLimitFlag;

    /**
     * 是否披露产品资料概要标志||0-否1-是
     */
    @Column(name = "reveal_prodt_doc_outl_flag")
    private String revealProdtDocOutlFlag;

    /**
     * 是否是专户理财产品||0-否；1-是
     */
    @Column(name = "spcacc_chmprdt_flag")
    private String spcaccChmprdtFlag;

    /**
     * 是否我行托管标志|0-否，1-是
     */
    @Column(name = "psbc_trustee_flag")
    private String psbcTrusteeFlag;

    /**
     * 是否香港互认基金标志|0-否，1-是
     */
    @Column(name = "hk_mut_reco_fund_flag")
    private String hkMutRecoFundFlag;

    /**
     * 是否允许分红方式变更||0-允许；1-不允许
     */
    @Column(name = "divid_mode_code")
    private String dividModeCode;

    /**
     * 是否允许转换标志||1.关闭，2.转入，3.转出，4.转入转出
     */
    @Column(name = "permit_tranfm_flag")
    private String permitTranfmFlag;

    /**
     * 推荐指数参数||0-无星级、1-3星级、2-4星级、3-5星级、4-特别推荐
     */
    @Column(name = "recomend_exp_para_code")
    private String recomendExpParaCode;

    /**
     * 养老基金代码||0-目标日期基金,1-目标风险基金
     */
    @Column(name = "aged_fund_cd")
    private String agedFundCd;

    /**
     * 是否养老目标基金||0-否1-是
     */
    @Column(name = "aged_target_fund_flag")
    private String agedTargetFundFlag;

    /**
     * 养老基金封闭年数||养老基金封闭年数
     */
    @Column(name = "aged_fund_close_year_num")
    private Short agedFundCloseYearNum;

    /**
     * 优选基金标志0-否1-是
     */
    @Column(name = "optimiz_fund_flag")
    private String optimizFundFlag;

    /**
     * 热销基金标志0-否1-是
     */
    @Column(name = "hot_sale_fund_flag")
    private String hotSaleFundFlag;

    /**
     * 展示风险揭示书标志|0-否，1-是
     */
    @Column(name = "show_risk_revea_bk_flag")
    private String showRiskReveaBkFlag;

    /**
     * 转托管方式代码||0-一步转托管1-两步转托管2-禁止
     */
    @Column(name = "tran_trustee_mode")
    private String tranTrusteeMode;

    /**
     * 转托管状态代码||0-允许所有转托管1-仅允许场外转托管2-仅允许跨市场转托管3-禁止所有转托管
     */
    @Column(name = "tran_trustee_status")
    private String tranTrusteeStatus;

    /**
     * 总额度||总额度
     */
    @Column(name = "tot_limt")
    private BigDecimal totLimt;

    /**
     * 启用日期||启用日期
     */
    @Column(name = "enable_date")
    private String enableDate;

    /**
     * 确认时限
     */
    @Column(name = "cfm_term_val")
    private Short cfmTermVal;

    /**
     * 标签编号
     */
    @Column(name = "label_no")
    private String labelNo;

    /**
     * 产品描述
     */
    @Column(name = "prodt_desc")
    private String prodtDesc;

    /**
     * 最后修改时间戳||最后修改时间戳
     */
    @Column(name = "last_mod_stamp")
    private String lastModStamp;
    /**
     * 产品特色额度||角色兼容说明
     */
    @Column(name = "role_compat_desc")
    private String roleCompatDesc;

    @Column(name = "put_shel_time")
    private String putShelTime;

    /**
     * 设置产品特色额度||角色兼容说明
     *
     * @param roleCompatDesc 产品特色额度||角色兼容说明
     */
    public void setRoleCompatDesc(String roleCompatDesc) {
        this.roleCompatDesc = roleCompatDesc == null ? null : roleCompatDesc.trim();
    }

    /**
     * 获取产品特色额度||角色兼容说明
     *
     * @return role_compat_desc - 产品特色额度||角色兼容说明
     */
    public String getRoleCompatDesc() {
        return roleCompatDesc;
    }

    private static final long serialVersionUID = 1L;

    /**
     * 获取基金产品编码	||基金产品编码
     *
     * @return fund_prodt_no - 基金产品编码	||基金产品编码
     */
    public String getFundProdtNo() {
        return fundProdtNo;
    }

    /**
     * 设置基金产品编码	||基金产品编码
     *
     * @param fundProdtNo 基金产品编码	||基金产品编码
     */
    public void setFundProdtNo(String fundProdtNo) {
        this.fundProdtNo = fundProdtNo == null ? null : fundProdtNo.trim();
    }

    /**
     * 获取登记机构号||TA登记机构
     *
     * @return enroll_inst_no - 登记机构号||TA登记机构
     */
    public String getEnrollInstNo() {
        return enrollInstNo;
    }

    /**
     * 设置登记机构号||TA登记机构
     *
     * @param enrollInstNo 登记机构号||TA登记机构
     */
    public void setEnrollInstNo(String enrollInstNo) {
        this.enrollInstNo = enrollInstNo == null ? null : enrollInstNo.trim();
    }

    /**
     * 获取所属机构号||产品所属机构
     *
     * @return belong_inst_no - 所属机构号||产品所属机构
     */
    public String getBelongInstNo() {
        return belongInstNo;
    }

    /**
     * 设置所属机构号||产品所属机构
     *
     * @param belongInstNo 所属机构号||产品所属机构
     */
    public void setBelongInstNo(String belongInstNo) {
        this.belongInstNo = belongInstNo == null ? null : belongInstNo.trim();
    }

    /**
     * 获取基金状态代码||0-可申购赎回，1-发行4-停止申购赎回5-停止申购，6-停止赎回8-基金终止，9-基金封闭
     *
     * @return fund_status_code - 基金状态代码||0-可申购赎回，1-发行4-停止申购赎回5-停止申购，6-停止赎回8-基金终止，9-基金封闭
     */
    public String getFundStatusCode() {
        return fundStatusCode;
    }

    /**
     * 设置基金状态代码||0-可申购赎回，1-发行4-停止申购赎回5-停止申购，6-停止赎回8-基金终止，9-基金封闭
     *
     * @param fundStatusCode 基金状态代码||0-可申购赎回，1-发行4-停止申购赎回5-停止申购，6-停止赎回8-基金终止，9-基金封闭
     */
    public void setFundStatusCode(String fundStatusCode) {
        this.fundStatusCode = fundStatusCode == null ? null : fundStatusCode.trim();
    }

    /**
     * 获取产品名称||产品名称
     *
     * @return prodt_name - 产品名称||产品名称
     */
    public String getProdtName() {
        return prodtName;
    }

    /**
     * 设置产品名称||产品名称
     *
     * @param prodtName 产品名称||产品名称
     */
    public void setProdtName(String prodtName) {
        this.prodtName = prodtName == null ? null : prodtName.trim();
    }

    /**
     * 获取募集起始日期|募集起始日期
     *
     * @return rais_bgn_date - 募集起始日期|募集起始日期
     */
    public String getRaisBgnDate() {
        return raisBgnDate;
    }

    /**
     * 设置募集起始日期|募集起始日期
     *
     * @param raisBgnDate 募集起始日期|募集起始日期
     */
    public void setRaisBgnDate(String raisBgnDate) {
        this.raisBgnDate = raisBgnDate == null ? null : raisBgnDate.trim();
    }

    /**
     * 获取募集结束日期|募集结束日期
     *
     * @return collect_end_dt - 募集结束日期|募集结束日期
     */
    public String getCollectEndDt() {
        return collectEndDt;
    }

    /**
     * 设置募集结束日期|募集结束日期
     *
     * @param collectEndDt 募集结束日期|募集结束日期
     */
    public void setCollectEndDt(String collectEndDt) {
        this.collectEndDt = collectEndDt == null ? null : collectEndDt.trim();
    }

    /**
     * 获取募集天数|募集天数
     *
     * @return collect_daynum - 募集天数|募集天数
     */
    public Long getCollectDaynum() {
        return collectDaynum;
    }

    /**
     * 设置募集天数|募集天数
     *
     * @param collectDaynum 募集天数|募集天数
     */
    public void setCollectDaynum(Long collectDaynum) {
        this.collectDaynum = collectDaynum;
    }

    /**
     * 获取收费方式||0-前端收费1-后端收费
     *
     * @return ctfee_mode_cd - 收费方式||0-前端收费1-后端收费
     */
    public String getCtfeeModeCd() {
        return ctfeeModeCd;
    }

    /**
     * 设置收费方式||0-前端收费1-后端收费
     *
     * @param ctfeeModeCd 收费方式||0-前端收费1-后端收费
     */
    public void setCtfeeModeCd(String ctfeeModeCd) {
        this.ctfeeModeCd = ctfeeModeCd == null ? null : ctfeeModeCd.trim();
    }

    /**
     * 获取基金类型||0-货币型基金1-股票型基金2-债券型基金3-混合型基金4-保本型基金	5-指数型6-短债型7-QDII
     *
     * @return fund_type - 基金类型||0-货币型基金1-股票型基金2-债券型基金3-混合型基金4-保本型基金	5-指数型6-短债型7-QDII
     */
    public String getFundType() {
        return fundType;
    }

    /**
     * 设置基金类型||0-货币型基金1-股票型基金2-债券型基金3-混合型基金4-保本型基金	5-指数型6-短债型7-QDII
     *
     * @param fundType 基金类型||0-货币型基金1-股票型基金2-债券型基金3-混合型基金4-保本型基金	5-指数型6-短债型7-QDII
     */
    public void setFundType(String fundType) {
        this.fundType = fundType == null ? null : fundType.trim();
    }

    /**
     * 获取基金二级分类||01-普通股票型、02-被动指数型、03-增强指数型、04-偏股混合型、05-平衡混合型、06-偏债混合型、07-灵活配置型、08-中长期纯债型、09-短期纯债型、10-混合债券型一级、11-混合债券型二级、12-被动指数债券型、13-增强指数债券型、14-传统货币型、15-短期理财型、16-浮动净值型货币、17-股票多空型、18-商品型、19-QDII股票型、20-QDII债券型、21-QDII混合型、22-QDII另类型、99-其他
     *
     * @return fund_lvl2_cls_cd - 基金二级分类||01-普通股票型、02-被动指数型、03-增强指数型、04-偏股混合型、05-平衡混合型、06-偏债混合型、07-灵活配置型、08-中长期纯债型、09-短期纯债型、10-混合债券型一级、11-混合债券型二级、12-被动指数债券型、13-增强指数债券型、14-传统货币型、15-短期理财型、16-浮动净值型货币、17-股票多空型、18-商品型、19-QDII股票型、20-QDII债券型、21-QDII混合型、22-QDII另类型、99-其他
     */
    public String getFundLvl2ClsCd() {
        return fundLvl2ClsCd;
    }

    /**
     * 设置基金二级分类||01-普通股票型、02-被动指数型、03-增强指数型、04-偏股混合型、05-平衡混合型、06-偏债混合型、07-灵活配置型、08-中长期纯债型、09-短期纯债型、10-混合债券型一级、11-混合债券型二级、12-被动指数债券型、13-增强指数债券型、14-传统货币型、15-短期理财型、16-浮动净值型货币、17-股票多空型、18-商品型、19-QDII股票型、20-QDII债券型、21-QDII混合型、22-QDII另类型、99-其他
     *
     * @param fundLvl2ClsCd 基金二级分类||01-普通股票型、02-被动指数型、03-增强指数型、04-偏股混合型、05-平衡混合型、06-偏债混合型、07-灵活配置型、08-中长期纯债型、09-短期纯债型、10-混合债券型一级、11-混合债券型二级、12-被动指数债券型、13-增强指数债券型、14-传统货币型、15-短期理财型、16-浮动净值型货币、17-股票多空型、18-商品型、19-QDII股票型、20-QDII债券型、21-QDII混合型、22-QDII另类型、99-其他
     */
    public void setFundLvl2ClsCd(String fundLvl2ClsCd) {
        this.fundLvl2ClsCd = fundLvl2ClsCd == null ? null : fundLvl2ClsCd.trim();
    }

    /**
     * 获取理财产品风险等级代码||1-低2-中低3-中4-中高5-高
     *
     * @return chrem_prodt_risk_lvl_code - 理财产品风险等级代码||1-低2-中低3-中4-中高5-高
     */
    public String getChremProdtRiskLvlCode() {
        return chremProdtRiskLvlCode;
    }

    /**
     * 设置理财产品风险等级代码||1-低2-中低3-中4-中高5-高
     *
     * @param chremProdtRiskLvlCode 理财产品风险等级代码||1-低2-中低3-中4-中高5-高
     */
    public void setChremProdtRiskLvlCode(String chremProdtRiskLvlCode) {
        this.chremProdtRiskLvlCode = chremProdtRiskLvlCode == null ? null : chremProdtRiskLvlCode.trim();
    }

    /**
     * 获取默认分红方式||0-红利转股1-现金分红
     *
     * @return defau_divid_mode - 默认分红方式||0-红利转股1-现金分红
     */
    public String getDefauDividMode() {
        return defauDividMode;
    }

    /**
     * 设置默认分红方式||0-红利转股1-现金分红
     *
     * @param defauDividMode 默认分红方式||0-红利转股1-现金分红
     */
    public void setDefauDividMode(String defauDividMode) {
        this.defauDividMode = defauDividMode == null ? null : defauDividMode.trim();
    }

    /**
     * 获取机构最低认购限额||机构最低认购限额
     *
     * @return inst_lowest_subscri_lmt_amt - 机构最低认购限额||机构最低认购限额
     */
    public BigDecimal getInstLowestSubscriLmtAmt() {
        return instLowestSubscriLmtAmt;
    }

    /**
     * 设置机构最低认购限额||机构最低认购限额
     *
     * @param instLowestSubscriLmtAmt 机构最低认购限额||机构最低认购限额
     */
    public void setInstLowestSubscriLmtAmt(BigDecimal instLowestSubscriLmtAmt) {
        this.instLowestSubscriLmtAmt = instLowestSubscriLmtAmt;
    }

    /**
     * 获取机构追加最低认购限额||机构追加最低认购限额
     *
     * @return instaddto_lowest_subscri_lmamt - 机构追加最低认购限额||机构追加最低认购限额
     */
    public BigDecimal getInstaddtoLowestSubscriLmamt() {
        return instaddtoLowestSubscriLmamt;
    }

    /**
     * 设置机构追加最低认购限额||机构追加最低认购限额
     *
     * @param instaddtoLowestSubscriLmamt 机构追加最低认购限额||机构追加最低认购限额
     */
    public void setInstaddtoLowestSubscriLmamt(BigDecimal instaddtoLowestSubscriLmamt) {
        this.instaddtoLowestSubscriLmamt = instaddtoLowestSubscriLmamt;
    }

    /**
     * 获取机构最高认购限额||机构最高认购限额
     *
     * @return inst_highest_subscri_lmt_amt - 机构最高认购限额||机构最高认购限额
     */
    public BigDecimal getInstHighestSubscriLmtAmt() {
        return instHighestSubscriLmtAmt;
    }

    /**
     * 设置机构最高认购限额||机构最高认购限额
     *
     * @param instHighestSubscriLmtAmt 机构最高认购限额||机构最高认购限额
     */
    public void setInstHighestSubscriLmtAmt(BigDecimal instHighestSubscriLmtAmt) {
        this.instHighestSubscriLmtAmt = instHighestSubscriLmtAmt;
    }

    /**
     * 获取机构最低申购限额||机构最低申购限额
     *
     * @return inst_lowest_aplypchs_lmt_amt - 机构最低申购限额||机构最低申购限额
     */
    public BigDecimal getInstLowestAplypchsLmtAmt() {
        return instLowestAplypchsLmtAmt;
    }

    /**
     * 设置机构最低申购限额||机构最低申购限额
     *
     * @param instLowestAplypchsLmtAmt 机构最低申购限额||机构最低申购限额
     */
    public void setInstLowestAplypchsLmtAmt(BigDecimal instLowestAplypchsLmtAmt) {
        this.instLowestAplypchsLmtAmt = instLowestAplypchsLmtAmt;
    }

    /**
     * 获取机构追加最低申购限额||机构追加最低申购限额
     *
     * @return instaddto_lowe_aplypchs_lmamt - 机构追加最低申购限额||机构追加最低申购限额
     */
    public BigDecimal getInstaddtoLoweAplypchsLmamt() {
        return instaddtoLoweAplypchsLmamt;
    }

    /**
     * 设置机构追加最低申购限额||机构追加最低申购限额
     *
     * @param instaddtoLoweAplypchsLmamt 机构追加最低申购限额||机构追加最低申购限额
     */
    public void setInstaddtoLoweAplypchsLmamt(BigDecimal instaddtoLoweAplypchsLmamt) {
        this.instaddtoLoweAplypchsLmamt = instaddtoLoweAplypchsLmamt;
    }

    /**
     * 获取机构单笔最高申购限额||机构单笔最高申购限额
     *
     * @return inst_per_highest_aplyp_lmt_amt - 机构单笔最高申购限额||机构单笔最高申购限额
     */
    public BigDecimal getInstPerHighestAplypLmtAmt() {
        return instPerHighestAplypLmtAmt;
    }

    /**
     * 设置机构单笔最高申购限额||机构单笔最高申购限额
     *
     * @param instPerHighestAplypLmtAmt 机构单笔最高申购限额||机构单笔最高申购限额
     */
    public void setInstPerHighestAplypLmtAmt(BigDecimal instPerHighestAplypLmtAmt) {
        this.instPerHighestAplypLmtAmt = instPerHighestAplypLmtAmt;
    }

    /**
     * 获取机构最高申购限额||机构最高申购限额
     *
     * @return inst_highest_aplypchs_lmt_amt - 机构最高申购限额||机构最高申购限额
     */
    public BigDecimal getInstHighestAplypchsLmtAmt() {
        return instHighestAplypchsLmtAmt;
    }

    /**
     * 设置机构最高申购限额||机构最高申购限额
     *
     * @param instHighestAplypchsLmtAmt 机构最高申购限额||机构最高申购限额
     */
    public void setInstHighestAplypchsLmtAmt(BigDecimal instHighestAplypchsLmtAmt) {
        this.instHighestAplypchsLmtAmt = instHighestAplypchsLmtAmt;
    }

    /**
     * 获取机构最低赎回限额||机构最低赎回限额
     *
     * @return inst_lowest_redem_lmt_amt - 机构最低赎回限额||机构最低赎回限额
     */
    public BigDecimal getInstLowestRedemLmtAmt() {
        return instLowestRedemLmtAmt;
    }

    /**
     * 设置机构最低赎回限额||机构最低赎回限额
     *
     * @param instLowestRedemLmtAmt 机构最低赎回限额||机构最低赎回限额
     */
    public void setInstLowestRedemLmtAmt(BigDecimal instLowestRedemLmtAmt) {
        this.instLowestRedemLmtAmt = instLowestRedemLmtAmt;
    }

    /**
     * 获取机构单笔最高赎回限额||机构单笔最高赎回限额
     *
     * @return inst_per_highest_redem_lmt_amt - 机构单笔最高赎回限额||机构单笔最高赎回限额
     */
    public BigDecimal getInstPerHighestRedemLmtAmt() {
        return instPerHighestRedemLmtAmt;
    }

    /**
     * 设置机构单笔最高赎回限额||机构单笔最高赎回限额
     *
     * @param instPerHighestRedemLmtAmt 机构单笔最高赎回限额||机构单笔最高赎回限额
     */
    public void setInstPerHighestRedemLmtAmt(BigDecimal instPerHighestRedemLmtAmt) {
        this.instPerHighestRedemLmtAmt = instPerHighestRedemLmtAmt;
    }

    /**
     * 获取机构最高赎回限额||机构最高赎回限额
     *
     * @return inst_highest_redem_lmt_amt - 机构最高赎回限额||机构最高赎回限额
     */
    public BigDecimal getInstHighestRedemLmtAmt() {
        return instHighestRedemLmtAmt;
    }

    /**
     * 设置机构最高赎回限额||机构最高赎回限额
     *
     * @param instHighestRedemLmtAmt 机构最高赎回限额||机构最高赎回限额
     */
    public void setInstHighestRedemLmtAmt(BigDecimal instHighestRedemLmtAmt) {
        this.instHighestRedemLmtAmt = instHighestRedemLmtAmt;
    }

    /**
     * 获取机构最低持有限额||机构最低持有限额
     *
     * @return inst_lowest_hold_have_lmt_amt - 机构最低持有限额||机构最低持有限额
     */
    public BigDecimal getInstLowestHoldHaveLmtAmt() {
        return instLowestHoldHaveLmtAmt;
    }

    /**
     * 设置机构最低持有限额||机构最低持有限额
     *
     * @param instLowestHoldHaveLmtAmt 机构最低持有限额||机构最低持有限额
     */
    public void setInstLowestHoldHaveLmtAmt(BigDecimal instLowestHoldHaveLmtAmt) {
        this.instLowestHoldHaveLmtAmt = instLowestHoldHaveLmtAmt;
    }

    /**
     * 获取基金转换下限金额||基金转换下限金额
     *
     * @return fund_tranfm_low_lmt_amt - 基金转换下限金额||基金转换下限金额
     */
    public BigDecimal getFundTranfmLowLmtAmt() {
        return fundTranfmLowLmtAmt;
    }

    /**
     * 设置基金转换下限金额||基金转换下限金额
     *
     * @param fundTranfmLowLmtAmt 基金转换下限金额||基金转换下限金额
     */
    public void setFundTranfmLowLmtAmt(BigDecimal fundTranfmLowLmtAmt) {
        this.fundTranfmLowLmtAmt = fundTranfmLowLmtAmt;
    }

    /**
     * 获取定期定额申购上限金额||定期定额申购上限金额
     *
     * @return fix_quota_aplypchs_up_lmt_amt - 定期定额申购上限金额||定期定额申购上限金额
     */
    public BigDecimal getFixQuotaAplypchsUpLmtAmt() {
        return fixQuotaAplypchsUpLmtAmt;
    }

    /**
     * 设置定期定额申购上限金额||定期定额申购上限金额
     *
     * @param fixQuotaAplypchsUpLmtAmt 定期定额申购上限金额||定期定额申购上限金额
     */
    public void setFixQuotaAplypchsUpLmtAmt(BigDecimal fixQuotaAplypchsUpLmtAmt) {
        this.fixQuotaAplypchsUpLmtAmt = fixQuotaAplypchsUpLmtAmt;
    }

    /**
     * 获取定期定额申购下限金额||定期定额申购下限金额
     *
     * @return fix_quota_aplypchs_low_lmt_amt - 定期定额申购下限金额||定期定额申购下限金额
     */
    public BigDecimal getFixQuotaAplypchsLowLmtAmt() {
        return fixQuotaAplypchsLowLmtAmt;
    }

    /**
     * 设置定期定额申购下限金额||定期定额申购下限金额
     *
     * @param fixQuotaAplypchsLowLmtAmt 定期定额申购下限金额||定期定额申购下限金额
     */
    public void setFixQuotaAplypchsLowLmtAmt(BigDecimal fixQuotaAplypchsLowLmtAmt) {
        this.fixQuotaAplypchsLowLmtAmt = fixQuotaAplypchsLowLmtAmt;
    }

    /**
     * 获取拼音名称||拼音名称全拼
     *
     * @return en_py_name - 拼音名称||拼音名称全拼
     */
    public String getEnPyName() {
        return enPyName;
    }

    /**
     * 设置拼音名称||拼音名称全拼
     *
     * @param enPyName 拼音名称||拼音名称全拼
     */
    public void setEnPyName(String enPyName) {
        this.enPyName = enPyName == null ? null : enPyName.trim();
    }

    /**
     * 获取拼音简称||产品拼音首字母
     *
     * @return en_py_sname - 拼音简称||产品拼音首字母
     */
    public String getEnPySname() {
        return enPySname;
    }

    /**
     * 设置拼音简称||产品拼音首字母
     *
     * @param enPySname 拼音简称||产品拼音首字母
     */
    public void setEnPySname(String enPySname) {
        this.enPySname = enPySname == null ? null : enPySname.trim();
    }

    /**
     * 获取产品名称||基金产品全称
     *
     * @return prodt_aname - 产品名称||基金产品全称
     */
    public String getProdtAname() {
        return prodtAname;
    }

    /**
     * 设置产品名称||基金产品全称
     *
     * @param prodtAname 产品名称||基金产品全称
     */
    public void setProdtAname(String prodtAname) {
        this.prodtAname = prodtAname == null ? null : prodtAname.trim();
    }

    /**
     * 获取基金托管银行||基金托管银行
     *
     * @return fund_trustee_bank_name - 基金托管银行||基金托管银行
     */
    public String getFundTrusteeBankName() {
        return fundTrusteeBankName;
    }

    /**
     * 设置基金托管银行||基金托管银行
     *
     * @param fundTrusteeBankName 基金托管银行||基金托管银行
     */
    public void setFundTrusteeBankName(String fundTrusteeBankName) {
        this.fundTrusteeBankName = fundTrusteeBankName == null ? null : fundTrusteeBankName.trim();
    }

    /**
     * 获取短期理财标志||0-否1-是
     *
     * @return chrem_flag - 短期理财标志||0-否1-是
     */
    public String getChremFlag() {
        return chremFlag;
    }

    /**
     * 设置短期理财标志||0-否1-是
     *
     * @param chremFlag 短期理财标志||0-否1-是
     */
    public void setChremFlag(String chremFlag) {
        this.chremFlag = chremFlag == null ? null : chremFlag.trim();
    }

    /**
     * 获取合同类型代码||0-纸质；1-电子
     *
     * @return contract_type - 合同类型代码||0-纸质；1-电子
     */
    public String getContractType() {
        return contractType;
    }

    /**
     * 设置合同类型代码||0-纸质；1-电子
     *
     * @param contractType 合同类型代码||0-纸质；1-电子
     */
    public void setContractType(String contractType) {
        this.contractType = contractType == null ? null : contractType.trim();
    }

    /**
     * 获取合同名称||业务合同名称
     *
     * @return busi_contract_name - 合同名称||业务合同名称
     */
    public String getBusiContractName() {
        return busiContractName;
    }

    /**
     * 设置合同名称||业务合同名称
     *
     * @param busiContractName 合同名称||业务合同名称
     */
    public void setBusiContractName(String busiContractName) {
        this.busiContractName = busiContractName == null ? null : busiContractName.trim();
    }

    /**
     * 获取电子确认方代码||0-基金公司确认1-中登确认
     *
     * @return e_conf_par_code - 电子确认方代码||0-基金公司确认1-中登确认
     */
    public String geteConfParCode() {
        return eConfParCode;
    }

    /**
     * 设置电子确认方代码||0-基金公司确认1-中登确认
     *
     * @param eConfParCode 电子确认方代码||0-基金公司确认1-中登确认
     */
    public void seteConfParCode(String eConfParCode) {
        this.eConfParCode = eConfParCode == null ? null : eConfParCode.trim();
    }

    /**
     * 获取是否是lof基金标志||0-非lof基金1-lof基金
     *
     * @return lof_fund_flag - 是否是lof基金标志||0-非lof基金1-lof基金
     */
    public String getLofFundFlag() {
        return lofFundFlag;
    }

    /**
     * 设置是否是lof基金标志||0-非lof基金1-lof基金
     *
     * @param lofFundFlag 是否是lof基金标志||0-非lof基金1-lof基金
     */
    public void setLofFundFlag(String lofFundFlag) {
        this.lofFundFlag = lofFundFlag == null ? null : lofFundFlag.trim();
    }

    /**
     * 获取某年持有基金标志|0-否，1-是
     *
     * @return partc_yr_hold_hv_fund_flag - 某年持有基金标志|0-否，1-是
     */
    public String getPartcYrHoldHvFundFlag() {
        return partcYrHoldHvFundFlag;
    }

    /**
     * 设置某年持有基金标志|0-否，1-是
     *
     * @param partcYrHoldHvFundFlag 某年持有基金标志|0-否，1-是
     */
    public void setPartcYrHoldHvFundFlag(String partcYrHoldHvFundFlag) {
        this.partcYrHoldHvFundFlag = partcYrHoldHvFundFlag == null ? null : partcYrHoldHvFundFlag.trim();
    }

    /**
     * 获取是否被退出标志||0-正常1-已退出
     *
     * @return byquit_flag - 是否被退出标志||0-正常1-已退出
     */
    public String getByquitFlag() {
        return byquitFlag;
    }

    /**
     * 设置是否被退出标志||0-正常1-已退出
     *
     * @param byquitFlag 是否被退出标志||0-正常1-已退出
     */
    public void setByquitFlag(String byquitFlag) {
        this.byquitFlag = byquitFlag == null ? null : byquitFlag.trim();
    }

    /**
     * 获取是否关联方管理产品标志|0-否，1-是
     *
     * @return rel_pty_manage_prodt_flag - 是否关联方管理产品标志|0-否，1-是
     */
    public String getRelPtyManageProdtFlag() {
        return relPtyManageProdtFlag;
    }

    /**
     * 设置是否关联方管理产品标志|0-否，1-是
     *
     * @param relPtyManageProdtFlag 是否关联方管理产品标志|0-否，1-是
     */
    public void setRelPtyManageProdtFlag(String relPtyManageProdtFlag) {
        this.relPtyManageProdtFlag = relPtyManageProdtFlag == null ? null : relPtyManageProdtFlag.trim();
    }

    /**
     * 获取是否集合理财产品标志||0-否1-是
     *
     * @return sets_chmprdt_flag - 是否集合理财产品标志||0-否1-是
     */
    public String getSetsChmprdtFlag() {
        return setsChmprdtFlag;
    }

    /**
     * 设置是否集合理财产品标志||0-否1-是
     *
     * @param setsChmprdtFlag 是否集合理财产品标志||0-否1-是
     */
    public void setSetsChmprdtFlag(String setsChmprdtFlag) {
        this.setsChmprdtFlag = setsChmprdtFlag == null ? null : setsChmprdtFlag.trim();
    }

    /**
     * 获取是否结构化产品标志|0-否，1-是
     *
     * @return structur_prodt_flag - 是否结构化产品标志|0-否，1-是
     */
    public String getStructurProdtFlag() {
        return structurProdtFlag;
    }

    /**
     * 设置是否结构化产品标志|0-否，1-是
     *
     * @param structurProdtFlag 是否结构化产品标志|0-否，1-是
     */
    public void setStructurProdtFlag(String structurProdtFlag) {
        this.structurProdtFlag = structurProdtFlag == null ? null : structurProdtFlag.trim();
    }

    /**
     * 获取是否开办定期定额业务||0–开办1–不开办
     *
     * @return open_fix_quota_busi_flag - 是否开办定期定额业务||0–开办1–不开办
     */
    public String getOpenFixQuotaBusiFlag() {
        return openFixQuotaBusiFlag;
    }

    /**
     * 设置是否开办定期定额业务||0–开办1–不开办
     *
     * @param openFixQuotaBusiFlag 是否开办定期定额业务||0–开办1–不开办
     */
    public void setOpenFixQuotaBusiFlag(String openFixQuotaBusiFlag) {
        this.openFixQuotaBusiFlag = openFixQuotaBusiFlag == null ? null : openFixQuotaBusiFlag.trim();
    }

    /**
     * 获取是否控制额度||0-允许；1-不允许
     *
     * @return ctrl_limit_flag - 是否控制额度||0-允许；1-不允许
     */
    public String getCtrlLimitFlag() {
        return ctrlLimitFlag;
    }

    /**
     * 设置是否控制额度||0-允许；1-不允许
     *
     * @param ctrlLimitFlag 是否控制额度||0-允许；1-不允许
     */
    public void setCtrlLimitFlag(String ctrlLimitFlag) {
        this.ctrlLimitFlag = ctrlLimitFlag == null ? null : ctrlLimitFlag.trim();
    }

    /**
     * 获取是否披露产品资料概要标志||0-否1-是
     *
     * @return reveal_prodt_doc_outl_flag - 是否披露产品资料概要标志||0-否1-是
     */
    public String getRevealProdtDocOutlFlag() {
        return revealProdtDocOutlFlag;
    }

    /**
     * 设置是否披露产品资料概要标志||0-否1-是
     *
     * @param revealProdtDocOutlFlag 是否披露产品资料概要标志||0-否1-是
     */
    public void setRevealProdtDocOutlFlag(String revealProdtDocOutlFlag) {
        this.revealProdtDocOutlFlag = revealProdtDocOutlFlag == null ? null : revealProdtDocOutlFlag.trim();
    }

    /**
     * 获取是否是专户理财产品||0-否；1-是
     *
     * @return spcacc_chmprdt_flag - 是否是专户理财产品||0-否；1-是
     */
    public String getSpcaccChmprdtFlag() {
        return spcaccChmprdtFlag;
    }

    /**
     * 设置是否是专户理财产品||0-否；1-是
     *
     * @param spcaccChmprdtFlag 是否是专户理财产品||0-否；1-是
     */
    public void setSpcaccChmprdtFlag(String spcaccChmprdtFlag) {
        this.spcaccChmprdtFlag = spcaccChmprdtFlag == null ? null : spcaccChmprdtFlag.trim();
    }

    /**
     * 获取是否我行托管标志|0-否，1-是
     *
     * @return psbc_trustee_flag - 是否我行托管标志|0-否，1-是
     */
    public String getPsbcTrusteeFlag() {
        return psbcTrusteeFlag;
    }

    /**
     * 设置是否我行托管标志|0-否，1-是
     *
     * @param psbcTrusteeFlag 是否我行托管标志|0-否，1-是
     */
    public void setPsbcTrusteeFlag(String psbcTrusteeFlag) {
        this.psbcTrusteeFlag = psbcTrusteeFlag == null ? null : psbcTrusteeFlag.trim();
    }

    /**
     * 获取是否香港互认基金标志|0-否，1-是
     *
     * @return hk_mut_reco_fund_flag - 是否香港互认基金标志|0-否，1-是
     */
    public String getHkMutRecoFundFlag() {
        return hkMutRecoFundFlag;
    }

    /**
     * 设置是否香港互认基金标志|0-否，1-是
     *
     * @param hkMutRecoFundFlag 是否香港互认基金标志|0-否，1-是
     */
    public void setHkMutRecoFundFlag(String hkMutRecoFundFlag) {
        this.hkMutRecoFundFlag = hkMutRecoFundFlag == null ? null : hkMutRecoFundFlag.trim();
    }

    /**
     * 获取是否允许分红方式变更||0-允许；1-不允许
     *
     * @return divid_mode_code - 是否允许分红方式变更||0-允许；1-不允许
     */
    public String getDividModeCode() {
        return dividModeCode;
    }

    /**
     * 设置是否允许分红方式变更||0-允许；1-不允许
     *
     * @param dividModeCode 是否允许分红方式变更||0-允许；1-不允许
     */
    public void setDividModeCode(String dividModeCode) {
        this.dividModeCode = dividModeCode == null ? null : dividModeCode.trim();
    }

    /**
     * 获取是否允许转换标志||1.关闭，2.转入，3.转出，4.转入转出
     *
     * @return permit_tranfm_flag - 是否允许转换标志||1.关闭，2.转入，3.转出，4.转入转出
     */
    public String getPermitTranfmFlag() {
        return permitTranfmFlag;
    }

    /**
     * 设置是否允许转换标志||1.关闭，2.转入，3.转出，4.转入转出
     *
     * @param permitTranfmFlag 是否允许转换标志||1.关闭，2.转入，3.转出，4.转入转出
     */
    public void setPermitTranfmFlag(String permitTranfmFlag) {
        this.permitTranfmFlag = permitTranfmFlag == null ? null : permitTranfmFlag.trim();
    }

    /**
     * 获取推荐指数参数||0-无星级、1-3星级、2-4星级、3-5星级、4-特别推荐
     *
     * @return recomend_exp_para_code - 推荐指数参数||0-无星级、1-3星级、2-4星级、3-5星级、4-特别推荐
     */
    public String getRecomendExpParaCode() {
        return recomendExpParaCode;
    }

    /**
     * 设置推荐指数参数||0-无星级、1-3星级、2-4星级、3-5星级、4-特别推荐
     *
     * @param recomendExpParaCode 推荐指数参数||0-无星级、1-3星级、2-4星级、3-5星级、4-特别推荐
     */
    public void setRecomendExpParaCode(String recomendExpParaCode) {
        this.recomendExpParaCode = recomendExpParaCode == null ? null : recomendExpParaCode.trim();
    }

    /**
     * 获取养老基金代码||0-目标日期基金,1-目标风险基金
     *
     * @return aged_fund_cd - 养老基金代码||0-目标日期基金,1-目标风险基金
     */
    public String getAgedFundCd() {
        return agedFundCd;
    }

    /**
     * 设置养老基金代码||0-目标日期基金,1-目标风险基金
     *
     * @param agedFundCd 养老基金代码||0-目标日期基金,1-目标风险基金
     */
    public void setAgedFundCd(String agedFundCd) {
        this.agedFundCd = agedFundCd == null ? null : agedFundCd.trim();
    }

    /**
     * 获取是否养老目标基金||0-否1-是
     *
     * @return aged_target_fund_flag - 是否养老目标基金||0-否1-是
     */
    public String getAgedTargetFundFlag() {
        return agedTargetFundFlag;
    }

    /**
     * 设置是否养老目标基金||0-否1-是
     *
     * @param agedTargetFundFlag 是否养老目标基金||0-否1-是
     */
    public void setAgedTargetFundFlag(String agedTargetFundFlag) {
        this.agedTargetFundFlag = agedTargetFundFlag == null ? null : agedTargetFundFlag.trim();
    }

    /**
     * 获取养老基金封闭年数||养老基金封闭年数
     *
     * @return aged_fund_close_year_num - 养老基金封闭年数||养老基金封闭年数
     */
    public Short getAgedFundCloseYearNum() {
        return agedFundCloseYearNum;
    }

    /**
     * 设置养老基金封闭年数||养老基金封闭年数
     *
     * @param agedFundCloseYearNum 养老基金封闭年数||养老基金封闭年数
     */
    public void setAgedFundCloseYearNum(Short agedFundCloseYearNum) {
        this.agedFundCloseYearNum = agedFundCloseYearNum;
    }

    /**
     * 获取优选基金标志0-否1-是
     *
     * @return optimiz_fund_flag - 优选基金标志0-否1-是
     */
    public String getOptimizFundFlag() {
        return optimizFundFlag;
    }

    /**
     * 设置优选基金标志0-否1-是
     *
     * @param optimizFundFlag 优选基金标志0-否1-是
     */
    public void setOptimizFundFlag(String optimizFundFlag) {
        this.optimizFundFlag = optimizFundFlag == null ? null : optimizFundFlag.trim();
    }

    /**
     * 获取热销基金标志0-否1-是
     *
     * @return hot_sale_fund_flag - 热销基金标志0-否1-是
     */
    public String getHotSaleFundFlag() {
        return hotSaleFundFlag;
    }

    /**
     * 设置热销基金标志0-否1-是
     *
     * @param hotSaleFundFlag 热销基金标志0-否1-是
     */
    public void setHotSaleFundFlag(String hotSaleFundFlag) {
        this.hotSaleFundFlag = hotSaleFundFlag == null ? null : hotSaleFundFlag.trim();
    }

    /**
     * 获取展示风险揭示书标志|0-否，1-是
     *
     * @return show_risk_revea_bk_flag - 展示风险揭示书标志|0-否，1-是
     */
    public String getShowRiskReveaBkFlag() {
        return showRiskReveaBkFlag;
    }

    /**
     * 设置展示风险揭示书标志|0-否，1-是
     *
     * @param showRiskReveaBkFlag 展示风险揭示书标志|0-否，1-是
     */
    public void setShowRiskReveaBkFlag(String showRiskReveaBkFlag) {
        this.showRiskReveaBkFlag = showRiskReveaBkFlag == null ? null : showRiskReveaBkFlag.trim();
    }

    /**
     * 获取转托管方式代码||0-一步转托管1-两步转托管2-禁止
     *
     * @return tran_trustee_mode - 转托管方式代码||0-一步转托管1-两步转托管2-禁止
     */
    public String getTranTrusteeMode() {
        return tranTrusteeMode;
    }

    /**
     * 设置转托管方式代码||0-一步转托管1-两步转托管2-禁止
     *
     * @param tranTrusteeMode 转托管方式代码||0-一步转托管1-两步转托管2-禁止
     */
    public void setTranTrusteeMode(String tranTrusteeMode) {
        this.tranTrusteeMode = tranTrusteeMode == null ? null : tranTrusteeMode.trim();
    }

    /**
     * 获取转托管状态代码||0-允许所有转托管1-仅允许场外转托管2-仅允许跨市场转托管3-禁止所有转托管
     *
     * @return tran_trustee_status - 转托管状态代码||0-允许所有转托管1-仅允许场外转托管2-仅允许跨市场转托管3-禁止所有转托管
     */
    public String getTranTrusteeStatus() {
        return tranTrusteeStatus;
    }

    /**
     * 设置转托管状态代码||0-允许所有转托管1-仅允许场外转托管2-仅允许跨市场转托管3-禁止所有转托管
     *
     * @param tranTrusteeStatus 转托管状态代码||0-允许所有转托管1-仅允许场外转托管2-仅允许跨市场转托管3-禁止所有转托管
     */
    public void setTranTrusteeStatus(String tranTrusteeStatus) {
        this.tranTrusteeStatus = tranTrusteeStatus == null ? null : tranTrusteeStatus.trim();
    }

    /**
     * 获取总额度||总额度
     *
     * @return tot_limt - 总额度||总额度
     */
    public BigDecimal getTotLimt() {
        return totLimt;
    }

    /**
     * 设置总额度||总额度
     *
     * @param totLimt 总额度||总额度
     */
    public void setTotLimt(BigDecimal totLimt) {
        this.totLimt = totLimt;
    }

    /**
     * 获取启用日期||启用日期
     *
     * @return enable_date - 启用日期||启用日期
     */
    public String getEnableDate() {
        return enableDate;
    }

    /**
     * 设置启用日期||启用日期
     *
     * @param enableDate 启用日期||启用日期
     */
    public void setEnableDate(String enableDate) {
        this.enableDate = enableDate == null ? null : enableDate.trim();
    }

    /**
     * 获取确认时限
     *
     * @return cfm_term_val - 确认时限
     */
    public Short getCfmTermVal() {
        return cfmTermVal;
    }

    /**
     * 设置确认时限
     *
     * @param cfmTermVal 确认时限
     */
    public void setCfmTermVal(Short cfmTermVal) {
        this.cfmTermVal = cfmTermVal;
    }

    /**
     * 获取标签编号
     *
     * @return label_no - 标签编号
     */
    public String getLabelNo() {
        return labelNo;
    }

    /**
     * 设置标签编号
     *
     * @param labelNo 标签编号
     */
    public void setLabelNo(String labelNo) {
        this.labelNo = labelNo == null ? null : labelNo.trim();
    }

    /**
     * 获取产品描述
     *
     * @return prodt_desc - 产品描述
     */
    public String getProdtDesc() {
        return prodtDesc;
    }

    /**
     * 设置产品描述
     *
     * @param prodtDesc 产品描述
     */
    public void setProdtDesc(String prodtDesc) {
        this.prodtDesc = prodtDesc == null ? null : prodtDesc.trim();
    }

    /**
     * 获取最后修改时间戳||最后修改时间戳
     *
     * @return last_mod_stamp - 最后修改时间戳||最后修改时间戳
     */
    public String getLastModStamp() {
        return lastModStamp;
    }

    /**
     * 设置最后修改时间戳||最后修改时间戳
     *
     * @param lastModStamp 最后修改时间戳||最后修改时间戳
     */
    public void setLastModStamp(String lastModStamp) {
        this.lastModStamp = lastModStamp == null ? null : lastModStamp.trim();
    }

    public String getPutShelTime() {
        return putShelTime;
    }

    public void setPutShelTime(String putShelTime) {
        this.putShelTime = putShelTime;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        FdIntbankKindpara other = (FdIntbankKindpara) that;
        return (this.getFundProdtNo() == null ? other.getFundProdtNo() == null : this.getFundProdtNo().equals(other.getFundProdtNo()))
                && (this.getEnrollInstNo() == null ? other.getEnrollInstNo() == null : this.getEnrollInstNo().equals(other.getEnrollInstNo()))
                && (this.getBelongInstNo() == null ? other.getBelongInstNo() == null : this.getBelongInstNo().equals(other.getBelongInstNo()))
                && (this.getFundStatusCode() == null ? other.getFundStatusCode() == null : this.getFundStatusCode().equals(other.getFundStatusCode()))
                && (this.getProdtName() == null ? other.getProdtName() == null : this.getProdtName().equals(other.getProdtName()))
                && (this.getRaisBgnDate() == null ? other.getRaisBgnDate() == null : this.getRaisBgnDate().equals(other.getRaisBgnDate()))
                && (this.getCollectEndDt() == null ? other.getCollectEndDt() == null : this.getCollectEndDt().equals(other.getCollectEndDt()))
                && (this.getCollectDaynum() == null ? other.getCollectDaynum() == null : this.getCollectDaynum().equals(other.getCollectDaynum()))
                && (this.getCtfeeModeCd() == null ? other.getCtfeeModeCd() == null : this.getCtfeeModeCd().equals(other.getCtfeeModeCd()))
                && (this.getFundType() == null ? other.getFundType() == null : this.getFundType().equals(other.getFundType()))
                && (this.getFundLvl2ClsCd() == null ? other.getFundLvl2ClsCd() == null : this.getFundLvl2ClsCd().equals(other.getFundLvl2ClsCd()))
                && (this.getChremProdtRiskLvlCode() == null ? other.getChremProdtRiskLvlCode() == null : this.getChremProdtRiskLvlCode().equals(other.getChremProdtRiskLvlCode()))
                && (this.getDefauDividMode() == null ? other.getDefauDividMode() == null : this.getDefauDividMode().equals(other.getDefauDividMode()))
                && (this.getInstLowestSubscriLmtAmt() == null ? other.getInstLowestSubscriLmtAmt() == null : this.getInstLowestSubscriLmtAmt().equals(other.getInstLowestSubscriLmtAmt()))
                && (this.getInstaddtoLowestSubscriLmamt() == null ? other.getInstaddtoLowestSubscriLmamt() == null : this.getInstaddtoLowestSubscriLmamt().equals(other.getInstaddtoLowestSubscriLmamt()))
                && (this.getInstHighestSubscriLmtAmt() == null ? other.getInstHighestSubscriLmtAmt() == null : this.getInstHighestSubscriLmtAmt().equals(other.getInstHighestSubscriLmtAmt()))
                && (this.getInstLowestAplypchsLmtAmt() == null ? other.getInstLowestAplypchsLmtAmt() == null : this.getInstLowestAplypchsLmtAmt().equals(other.getInstLowestAplypchsLmtAmt()))
                && (this.getInstaddtoLoweAplypchsLmamt() == null ? other.getInstaddtoLoweAplypchsLmamt() == null : this.getInstaddtoLoweAplypchsLmamt().equals(other.getInstaddtoLoweAplypchsLmamt()))
                && (this.getInstPerHighestAplypLmtAmt() == null ? other.getInstPerHighestAplypLmtAmt() == null : this.getInstPerHighestAplypLmtAmt().equals(other.getInstPerHighestAplypLmtAmt()))
                && (this.getInstHighestAplypchsLmtAmt() == null ? other.getInstHighestAplypchsLmtAmt() == null : this.getInstHighestAplypchsLmtAmt().equals(other.getInstHighestAplypchsLmtAmt()))
                && (this.getInstLowestRedemLmtAmt() == null ? other.getInstLowestRedemLmtAmt() == null : this.getInstLowestRedemLmtAmt().equals(other.getInstLowestRedemLmtAmt()))
                && (this.getInstPerHighestRedemLmtAmt() == null ? other.getInstPerHighestRedemLmtAmt() == null : this.getInstPerHighestRedemLmtAmt().equals(other.getInstPerHighestRedemLmtAmt()))
                && (this.getInstHighestRedemLmtAmt() == null ? other.getInstHighestRedemLmtAmt() == null : this.getInstHighestRedemLmtAmt().equals(other.getInstHighestRedemLmtAmt()))
                && (this.getInstLowestHoldHaveLmtAmt() == null ? other.getInstLowestHoldHaveLmtAmt() == null : this.getInstLowestHoldHaveLmtAmt().equals(other.getInstLowestHoldHaveLmtAmt()))
                && (this.getFundTranfmLowLmtAmt() == null ? other.getFundTranfmLowLmtAmt() == null : this.getFundTranfmLowLmtAmt().equals(other.getFundTranfmLowLmtAmt()))
                && (this.getFixQuotaAplypchsUpLmtAmt() == null ? other.getFixQuotaAplypchsUpLmtAmt() == null : this.getFixQuotaAplypchsUpLmtAmt().equals(other.getFixQuotaAplypchsUpLmtAmt()))
                && (this.getFixQuotaAplypchsLowLmtAmt() == null ? other.getFixQuotaAplypchsLowLmtAmt() == null : this.getFixQuotaAplypchsLowLmtAmt().equals(other.getFixQuotaAplypchsLowLmtAmt()))
                && (this.getEnPyName() == null ? other.getEnPyName() == null : this.getEnPyName().equals(other.getEnPyName()))
                && (this.getEnPySname() == null ? other.getEnPySname() == null : this.getEnPySname().equals(other.getEnPySname()))
                && (this.getProdtAname() == null ? other.getProdtAname() == null : this.getProdtAname().equals(other.getProdtAname()))
                && (this.getFundTrusteeBankName() == null ? other.getFundTrusteeBankName() == null : this.getFundTrusteeBankName().equals(other.getFundTrusteeBankName()))
                && (this.getChremFlag() == null ? other.getChremFlag() == null : this.getChremFlag().equals(other.getChremFlag()))
                && (this.getContractType() == null ? other.getContractType() == null : this.getContractType().equals(other.getContractType()))
                && (this.getBusiContractName() == null ? other.getBusiContractName() == null : this.getBusiContractName().equals(other.getBusiContractName()))
                && (this.geteConfParCode() == null ? other.geteConfParCode() == null : this.geteConfParCode().equals(other.geteConfParCode()))
                && (this.getLofFundFlag() == null ? other.getLofFundFlag() == null : this.getLofFundFlag().equals(other.getLofFundFlag()))
                && (this.getPartcYrHoldHvFundFlag() == null ? other.getPartcYrHoldHvFundFlag() == null : this.getPartcYrHoldHvFundFlag().equals(other.getPartcYrHoldHvFundFlag()))
                && (this.getByquitFlag() == null ? other.getByquitFlag() == null : this.getByquitFlag().equals(other.getByquitFlag()))
                && (this.getRelPtyManageProdtFlag() == null ? other.getRelPtyManageProdtFlag() == null : this.getRelPtyManageProdtFlag().equals(other.getRelPtyManageProdtFlag()))
                && (this.getSetsChmprdtFlag() == null ? other.getSetsChmprdtFlag() == null : this.getSetsChmprdtFlag().equals(other.getSetsChmprdtFlag()))
                && (this.getStructurProdtFlag() == null ? other.getStructurProdtFlag() == null : this.getStructurProdtFlag().equals(other.getStructurProdtFlag()))
                && (this.getOpenFixQuotaBusiFlag() == null ? other.getOpenFixQuotaBusiFlag() == null : this.getOpenFixQuotaBusiFlag().equals(other.getOpenFixQuotaBusiFlag()))
                && (this.getCtrlLimitFlag() == null ? other.getCtrlLimitFlag() == null : this.getCtrlLimitFlag().equals(other.getCtrlLimitFlag()))
                && (this.getRevealProdtDocOutlFlag() == null ? other.getRevealProdtDocOutlFlag() == null : this.getRevealProdtDocOutlFlag().equals(other.getRevealProdtDocOutlFlag()))
                && (this.getSpcaccChmprdtFlag() == null ? other.getSpcaccChmprdtFlag() == null : this.getSpcaccChmprdtFlag().equals(other.getSpcaccChmprdtFlag()))
                && (this.getPsbcTrusteeFlag() == null ? other.getPsbcTrusteeFlag() == null : this.getPsbcTrusteeFlag().equals(other.getPsbcTrusteeFlag()))
                && (this.getHkMutRecoFundFlag() == null ? other.getHkMutRecoFundFlag() == null : this.getHkMutRecoFundFlag().equals(other.getHkMutRecoFundFlag()))
                && (this.getDividModeCode() == null ? other.getDividModeCode() == null : this.getDividModeCode().equals(other.getDividModeCode()))
                && (this.getPermitTranfmFlag() == null ? other.getPermitTranfmFlag() == null : this.getPermitTranfmFlag().equals(other.getPermitTranfmFlag()))
                && (this.getRecomendExpParaCode() == null ? other.getRecomendExpParaCode() == null : this.getRecomendExpParaCode().equals(other.getRecomendExpParaCode()))
                && (this.getAgedFundCd() == null ? other.getAgedFundCd() == null : this.getAgedFundCd().equals(other.getAgedFundCd()))
                && (this.getAgedTargetFundFlag() == null ? other.getAgedTargetFundFlag() == null : this.getAgedTargetFundFlag().equals(other.getAgedTargetFundFlag()))
                && (this.getAgedFundCloseYearNum() == null ? other.getAgedFundCloseYearNum() == null : this.getAgedFundCloseYearNum().equals(other.getAgedFundCloseYearNum()))
                && (this.getOptimizFundFlag() == null ? other.getOptimizFundFlag() == null : this.getOptimizFundFlag().equals(other.getOptimizFundFlag()))
                && (this.getHotSaleFundFlag() == null ? other.getHotSaleFundFlag() == null : this.getHotSaleFundFlag().equals(other.getHotSaleFundFlag()))
                && (this.getShowRiskReveaBkFlag() == null ? other.getShowRiskReveaBkFlag() == null : this.getShowRiskReveaBkFlag().equals(other.getShowRiskReveaBkFlag()))
                && (this.getTranTrusteeMode() == null ? other.getTranTrusteeMode() == null : this.getTranTrusteeMode().equals(other.getTranTrusteeMode()))
                && (this.getTranTrusteeStatus() == null ? other.getTranTrusteeStatus() == null : this.getTranTrusteeStatus().equals(other.getTranTrusteeStatus()))
                && (this.getTotLimt() == null ? other.getTotLimt() == null : this.getTotLimt().equals(other.getTotLimt()))
                && (this.getEnableDate() == null ? other.getEnableDate() == null : this.getEnableDate().equals(other.getEnableDate()))
                && (this.getCfmTermVal() == null ? other.getCfmTermVal() == null : this.getCfmTermVal().equals(other.getCfmTermVal()))
                && (this.getLabelNo() == null ? other.getLabelNo() == null : this.getLabelNo().equals(other.getLabelNo()))
                && (this.getProdtDesc() == null ? other.getProdtDesc() == null : this.getProdtDesc().equals(other.getProdtDesc()))
                && (this.getLastModStamp() == null ? other.getLastModStamp() == null : this.getLastModStamp().equals(other.getLastModStamp()))
                && (this.getPutShelTime() == null ? other.getPutShelTime() == null : this.getPutShelTime().equals(other.getPutShelTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFundProdtNo() == null) ? 0 : getFundProdtNo().hashCode());
        result = prime * result + ((getEnrollInstNo() == null) ? 0 : getEnrollInstNo().hashCode());
        result = prime * result + ((getBelongInstNo() == null) ? 0 : getBelongInstNo().hashCode());
        result = prime * result + ((getFundStatusCode() == null) ? 0 : getFundStatusCode().hashCode());
        result = prime * result + ((getProdtName() == null) ? 0 : getProdtName().hashCode());
        result = prime * result + ((getRaisBgnDate() == null) ? 0 : getRaisBgnDate().hashCode());
        result = prime * result + ((getCollectEndDt() == null) ? 0 : getCollectEndDt().hashCode());
        result = prime * result + ((getCollectDaynum() == null) ? 0 : getCollectDaynum().hashCode());
        result = prime * result + ((getCtfeeModeCd() == null) ? 0 : getCtfeeModeCd().hashCode());
        result = prime * result + ((getFundType() == null) ? 0 : getFundType().hashCode());
        result = prime * result + ((getFundLvl2ClsCd() == null) ? 0 : getFundLvl2ClsCd().hashCode());
        result = prime * result + ((getChremProdtRiskLvlCode() == null) ? 0 : getChremProdtRiskLvlCode().hashCode());
        result = prime * result + ((getDefauDividMode() == null) ? 0 : getDefauDividMode().hashCode());
        result = prime * result + ((getInstLowestSubscriLmtAmt() == null) ? 0 : getInstLowestSubscriLmtAmt().hashCode());
        result = prime * result + ((getInstaddtoLowestSubscriLmamt() == null) ? 0 : getInstaddtoLowestSubscriLmamt().hashCode());
        result = prime * result + ((getInstHighestSubscriLmtAmt() == null) ? 0 : getInstHighestSubscriLmtAmt().hashCode());
        result = prime * result + ((getInstLowestAplypchsLmtAmt() == null) ? 0 : getInstLowestAplypchsLmtAmt().hashCode());
        result = prime * result + ((getInstaddtoLoweAplypchsLmamt() == null) ? 0 : getInstaddtoLoweAplypchsLmamt().hashCode());
        result = prime * result + ((getInstPerHighestAplypLmtAmt() == null) ? 0 : getInstPerHighestAplypLmtAmt().hashCode());
        result = prime * result + ((getInstHighestAplypchsLmtAmt() == null) ? 0 : getInstHighestAplypchsLmtAmt().hashCode());
        result = prime * result + ((getInstLowestRedemLmtAmt() == null) ? 0 : getInstLowestRedemLmtAmt().hashCode());
        result = prime * result + ((getInstPerHighestRedemLmtAmt() == null) ? 0 : getInstPerHighestRedemLmtAmt().hashCode());
        result = prime * result + ((getInstHighestRedemLmtAmt() == null) ? 0 : getInstHighestRedemLmtAmt().hashCode());
        result = prime * result + ((getInstLowestHoldHaveLmtAmt() == null) ? 0 : getInstLowestHoldHaveLmtAmt().hashCode());
        result = prime * result + ((getFundTranfmLowLmtAmt() == null) ? 0 : getFundTranfmLowLmtAmt().hashCode());
        result = prime * result + ((getFixQuotaAplypchsUpLmtAmt() == null) ? 0 : getFixQuotaAplypchsUpLmtAmt().hashCode());
        result = prime * result + ((getFixQuotaAplypchsLowLmtAmt() == null) ? 0 : getFixQuotaAplypchsLowLmtAmt().hashCode());
        result = prime * result + ((getEnPyName() == null) ? 0 : getEnPyName().hashCode());
        result = prime * result + ((getEnPySname() == null) ? 0 : getEnPySname().hashCode());
        result = prime * result + ((getProdtAname() == null) ? 0 : getProdtAname().hashCode());
        result = prime * result + ((getFundTrusteeBankName() == null) ? 0 : getFundTrusteeBankName().hashCode());
        result = prime * result + ((getChremFlag() == null) ? 0 : getChremFlag().hashCode());
        result = prime * result + ((getContractType() == null) ? 0 : getContractType().hashCode());
        result = prime * result + ((getBusiContractName() == null) ? 0 : getBusiContractName().hashCode());
        result = prime * result + ((geteConfParCode() == null) ? 0 : geteConfParCode().hashCode());
        result = prime * result + ((getLofFundFlag() == null) ? 0 : getLofFundFlag().hashCode());
        result = prime * result + ((getPartcYrHoldHvFundFlag() == null) ? 0 : getPartcYrHoldHvFundFlag().hashCode());
        result = prime * result + ((getByquitFlag() == null) ? 0 : getByquitFlag().hashCode());
        result = prime * result + ((getRelPtyManageProdtFlag() == null) ? 0 : getRelPtyManageProdtFlag().hashCode());
        result = prime * result + ((getSetsChmprdtFlag() == null) ? 0 : getSetsChmprdtFlag().hashCode());
        result = prime * result + ((getStructurProdtFlag() == null) ? 0 : getStructurProdtFlag().hashCode());
        result = prime * result + ((getOpenFixQuotaBusiFlag() == null) ? 0 : getOpenFixQuotaBusiFlag().hashCode());
        result = prime * result + ((getCtrlLimitFlag() == null) ? 0 : getCtrlLimitFlag().hashCode());
        result = prime * result + ((getRevealProdtDocOutlFlag() == null) ? 0 : getRevealProdtDocOutlFlag().hashCode());
        result = prime * result + ((getSpcaccChmprdtFlag() == null) ? 0 : getSpcaccChmprdtFlag().hashCode());
        result = prime * result + ((getPsbcTrusteeFlag() == null) ? 0 : getPsbcTrusteeFlag().hashCode());
        result = prime * result + ((getHkMutRecoFundFlag() == null) ? 0 : getHkMutRecoFundFlag().hashCode());
        result = prime * result + ((getDividModeCode() == null) ? 0 : getDividModeCode().hashCode());
        result = prime * result + ((getPermitTranfmFlag() == null) ? 0 : getPermitTranfmFlag().hashCode());
        result = prime * result + ((getRecomendExpParaCode() == null) ? 0 : getRecomendExpParaCode().hashCode());
        result = prime * result + ((getAgedFundCd() == null) ? 0 : getAgedFundCd().hashCode());
        result = prime * result + ((getAgedTargetFundFlag() == null) ? 0 : getAgedTargetFundFlag().hashCode());
        result = prime * result + ((getAgedFundCloseYearNum() == null) ? 0 : getAgedFundCloseYearNum().hashCode());
        result = prime * result + ((getOptimizFundFlag() == null) ? 0 : getOptimizFundFlag().hashCode());
        result = prime * result + ((getHotSaleFundFlag() == null) ? 0 : getHotSaleFundFlag().hashCode());
        result = prime * result + ((getShowRiskReveaBkFlag() == null) ? 0 : getShowRiskReveaBkFlag().hashCode());
        result = prime * result + ((getTranTrusteeMode() == null) ? 0 : getTranTrusteeMode().hashCode());
        result = prime * result + ((getTranTrusteeStatus() == null) ? 0 : getTranTrusteeStatus().hashCode());
        result = prime * result + ((getTotLimt() == null) ? 0 : getTotLimt().hashCode());
        result = prime * result + ((getEnableDate() == null) ? 0 : getEnableDate().hashCode());
        result = prime * result + ((getCfmTermVal() == null) ? 0 : getCfmTermVal().hashCode());
        result = prime * result + ((getLabelNo() == null) ? 0 : getLabelNo().hashCode());
        result = prime * result + ((getProdtDesc() == null) ? 0 : getProdtDesc().hashCode());
        result = prime * result + ((getLastModStamp() == null) ? 0 : getLastModStamp().hashCode());
        result = prime * result + ((getPutShelTime() == null) ? 0 : getPutShelTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", fundProdtNo=").append(fundProdtNo);
        sb.append(", enrollInstNo=").append(enrollInstNo);
        sb.append(", belongInstNo=").append(belongInstNo);
        sb.append(", fundStatusCode=").append(fundStatusCode);
        sb.append(", prodtName=").append(prodtName);
        sb.append(", raisBgnDate=").append(raisBgnDate);
        sb.append(", collectEndDt=").append(collectEndDt);
        sb.append(", collectDaynum=").append(collectDaynum);
        sb.append(", ctfeeModeCd=").append(ctfeeModeCd);
        sb.append(", fundType=").append(fundType);
        sb.append(", fundLvl2ClsCd=").append(fundLvl2ClsCd);
        sb.append(", chremProdtRiskLvlCode=").append(chremProdtRiskLvlCode);
        sb.append(", defauDividMode=").append(defauDividMode);
        sb.append(", instLowestSubscriLmtAmt=").append(instLowestSubscriLmtAmt);
        sb.append(", instaddtoLowestSubscriLmamt=").append(instaddtoLowestSubscriLmamt);
        sb.append(", instHighestSubscriLmtAmt=").append(instHighestSubscriLmtAmt);
        sb.append(", instLowestAplypchsLmtAmt=").append(instLowestAplypchsLmtAmt);
        sb.append(", instaddtoLoweAplypchsLmamt=").append(instaddtoLoweAplypchsLmamt);
        sb.append(", instPerHighestAplypLmtAmt=").append(instPerHighestAplypLmtAmt);
        sb.append(", instHighestAplypchsLmtAmt=").append(instHighestAplypchsLmtAmt);
        sb.append(", instLowestRedemLmtAmt=").append(instLowestRedemLmtAmt);
        sb.append(", instPerHighestRedemLmtAmt=").append(instPerHighestRedemLmtAmt);
        sb.append(", instHighestRedemLmtAmt=").append(instHighestRedemLmtAmt);
        sb.append(", instLowestHoldHaveLmtAmt=").append(instLowestHoldHaveLmtAmt);
        sb.append(", fundTranfmLowLmtAmt=").append(fundTranfmLowLmtAmt);
        sb.append(", fixQuotaAplypchsUpLmtAmt=").append(fixQuotaAplypchsUpLmtAmt);
        sb.append(", fixQuotaAplypchsLowLmtAmt=").append(fixQuotaAplypchsLowLmtAmt);
        sb.append(", enPyName=").append(enPyName);
        sb.append(", enPySname=").append(enPySname);
        sb.append(", prodtAname=").append(prodtAname);
        sb.append(", fundTrusteeBankName=").append(fundTrusteeBankName);
        sb.append(", chremFlag=").append(chremFlag);
        sb.append(", contractType=").append(contractType);
        sb.append(", busiContractName=").append(busiContractName);
        sb.append(", eConfParCode=").append(eConfParCode);
        sb.append(", lofFundFlag=").append(lofFundFlag);
        sb.append(", partcYrHoldHvFundFlag=").append(partcYrHoldHvFundFlag);
        sb.append(", byquitFlag=").append(byquitFlag);
        sb.append(", relPtyManageProdtFlag=").append(relPtyManageProdtFlag);
        sb.append(", setsChmprdtFlag=").append(setsChmprdtFlag);
        sb.append(", structurProdtFlag=").append(structurProdtFlag);
        sb.append(", openFixQuotaBusiFlag=").append(openFixQuotaBusiFlag);
        sb.append(", ctrlLimitFlag=").append(ctrlLimitFlag);
        sb.append(", revealProdtDocOutlFlag=").append(revealProdtDocOutlFlag);
        sb.append(", spcaccChmprdtFlag=").append(spcaccChmprdtFlag);
        sb.append(", psbcTrusteeFlag=").append(psbcTrusteeFlag);
        sb.append(", hkMutRecoFundFlag=").append(hkMutRecoFundFlag);
        sb.append(", dividModeCode=").append(dividModeCode);
        sb.append(", permitTranfmFlag=").append(permitTranfmFlag);
        sb.append(", recomendExpParaCode=").append(recomendExpParaCode);
        sb.append(", agedFundCd=").append(agedFundCd);
        sb.append(", agedTargetFundFlag=").append(agedTargetFundFlag);
        sb.append(", agedFundCloseYearNum=").append(agedFundCloseYearNum);
        sb.append(", optimizFundFlag=").append(optimizFundFlag);
        sb.append(", hotSaleFundFlag=").append(hotSaleFundFlag);
        sb.append(", showRiskReveaBkFlag=").append(showRiskReveaBkFlag);
        sb.append(", tranTrusteeMode=").append(tranTrusteeMode);
        sb.append(", tranTrusteeStatus=").append(tranTrusteeStatus);
        sb.append(", totLimt=").append(totLimt);
        sb.append(", enableDate=").append(enableDate);
        sb.append(", cfmTermVal=").append(cfmTermVal);
        sb.append(", labelNo=").append(labelNo);
        sb.append(", prodtDesc=").append(prodtDesc);
        sb.append(", lastModStamp=").append(lastModStamp);
        sb.append(", putShelTime=").append(putShelTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}