package monitordemo.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class UseJobMonitor extends AbstractHandler {
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		new Job("Job") {
			
			@Override
			public IStatus run(IProgressMonitor monitor) {
				final long begin = System.currentTimeMillis();
				System.out.println("UseJobMonitor Begin: " + begin);
				int max = MonitorConstants.MAX;
				SubMonitor convert = SubMonitor.convert(monitor, max);
				int i = 0;
				long result = 0;
				while(i < max) {
					convert.setTaskName(Integer.toString(i));
					result += i;
					i++;
					convert.worked(1);
					if (convert.isCanceled()) {
						return Status.CANCEL_STATUS;
					}
				}
				final long showMe = result;
				window.getShell().getDisplay().asyncExec(new Runnable() {

					@Override
					public void run() {
						long end = System.currentTimeMillis();
						System.out.println("   UseJobMonitor Msg Box Opens: " + end);
						System.out.println("   UseJobMonitor Time Until Msg Box Open: " + (end - begin));
						MessageDialog.openInformation(
								window.getShell(),
								"Result",
								Long.toString(showMe));
					}
					
				});
				long end = System.currentTimeMillis();
				System.out.println("UseJobMonitor End: " + end);
				System.out.println("UseJobMonitor Duration: " + (end - begin));
				return Status.OK_STATUS;
			}
		}.schedule();
		return null;
	}
}
