package com.funny.combo.core.dto;


import com.funny.combo.core.extension.BizScenario;

/**
 * Command stands for a request from Client.
 * According CommandExecutor will help to handle the business logic. This is a classic Command Pattern
 *
 * @author fulan.zjf 2017年10月27日 下午12:28:24
 */
public class AbstractCommand extends AbstractDTO {

    private static final long serialVersionUID = 1L;

    private BizScenario bizScenario;

	public BizScenario getBizScenario() {
		return bizScenario;
	}

	public void setBizScenario(BizScenario bizScenario) {
		this.bizScenario = bizScenario;
	}
}
