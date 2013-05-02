package helloworld;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class PrintTasklet implements Tasklet {

	private String message;

	public void setMessage(String message) {
		this.message = message;
	}

	public ExitStatus execute() throws Exception {
		System.out.print(message);
		return ExitStatus.COMPLETED;
	}

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1)
			throws Exception {
		return RepeatStatus.FINISHED;
	}
}