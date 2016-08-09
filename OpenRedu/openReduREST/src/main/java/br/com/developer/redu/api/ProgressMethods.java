package br.com.developer.redu.api;

import java.util.List;

import br.com.developer.redu.models.Progress;

public interface ProgressMethods<T> {
	
	public List<T> getProgressByLecture(String lectureId, String userId);
	public void putProgress(Progress progress);
}

