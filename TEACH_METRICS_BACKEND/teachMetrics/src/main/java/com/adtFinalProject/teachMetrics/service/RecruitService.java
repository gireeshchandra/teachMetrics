package com.adtFinalProject.teachMetrics.service;

import java.util.HashMap;

import com.adtFinalProject.teachMetrics.model.SelectModel;

public interface RecruitService {

	public HashMap<String, Object> fetchCandidates();
	public HashMap<String, Object> selectCandidate(SelectModel sm);
	public HashMap<String, Object> fetchUserStatus(String emailId);
}
