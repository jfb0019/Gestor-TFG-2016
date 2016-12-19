package ubu.digit.ui.views;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

import ubu.digit.ui.components.NavigationBar;
import ubu.digit.util.ExternalProperties;

import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

public class UploadCsvView extends VerticalLayout implements View {

	private static final long serialVersionUID = 8460171059055033456L;

	public static final String VIEW_NAME = "upload";

	private Label userText;

	private Button logout;

	private Upload upload;

	private ProgressBar progress;

	private CsvReceiver csvReceiver;

	private Label state;

	private String serverPath;

	private ExternalProperties config;

	private String dirCsv;

	private String completeDir;

	public UploadCsvView() {
		setMargin(true);
		setSpacing(true);

		serverPath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		config = ExternalProperties.getInstance("/WEB-INF/classes/config.properties", false);
		dirCsv = config.getSetting("dataIn");
		completeDir = serverPath + dirCsv + "/";

		NavigationBar navBar = new NavigationBar();
		addComponent(navBar);

		userText = new Label();

		progress = new ProgressBar();
		progress.setWidth("300px");
		progress.setCaption("Progreso:");

		csvReceiver = new CsvReceiver();

		upload = new Upload("Subida de ficheros CSV", null);
		upload.setButtonCaption("Subir fichero");
		upload.setReceiver(csvReceiver);
		upload.addStartedListener(csvReceiver);
		upload.addProgressListener(csvReceiver);
		upload.addFinishedListener(csvReceiver);
		upload.addSucceededListener(csvReceiver);
		upload.addFailedListener(csvReceiver);

		state = new Label();
		state.setCaption("Estado:");
		state.setValue("Idle");

		logout = new Button("Desconectar", new LogoutListener());

		addComponents(userText, upload, progress, state, logout);
	}

	class CsvReceiver implements Receiver, StartedListener, ProgressListener, FinishedListener, SucceededListener,
			FailedListener {
		private static final long serialVersionUID = -1414096228596596894L;

		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {
			FileOutputStream fos = null;
			File file;
			try {
				file = new File(completeDir + filename);
				fos = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				return new NullOutputStream();
			}
			return fos;
		}

		private class NullOutputStream extends OutputStream {
			@Override
			public void write(int b) throws IOException {
			}
		}

		@Override
		public void uploadStarted(StartedEvent event) {
			if (event.getFilename().isEmpty() || event.getFilename() == null) {
				Notification.show("Error", "Seleccione un fichero primero.", Type.ERROR_MESSAGE);
				upload.interruptUpload();
				return;
			}

			if (!event.getFilename().endsWith(".csv")) {
				Notification.show("Error",
						"El formato del fichero no esta soportado. Seleccione un fichero con extensión .csv",
						Type.ERROR_MESSAGE);
				upload.interruptUpload();
				return;
			}

			progress.setValue(0.0f);
			state.setValue("Subiendo fichero");
		}

		@Override
		public void updateProgress(long readBytes, long contentLength) {
			progress.setValue(new Float(readBytes / (float) contentLength));
			state.setValue("Procesados " + readBytes + " bytes de un total de " + contentLength + " bytes.");

		}

		@Override
		public void uploadFinished(FinishedEvent event) {
			state.setValue("Idle");
		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {
			state.setValue("Subida de fichero " + event.getFilename() + " existosa.");
		}

		@Override
		public void uploadFailed(FailedEvent event) {
			state.setValue("Subida de fichero " + event.getFilename() + " fallida");
			File file = new File(completeDir + event.getFilename());
			if (file.exists())
				file.delete();
		}
	}

	private class LogoutListener implements Button.ClickListener {
		private static final long serialVersionUID = -6910251607481142610L;

		@Override
		public void buttonClick(ClickEvent event) {
			getSession().setAttribute("user", null);
			getUI().getNavigator().navigateTo(InformationView.VIEW_NAME);
			Notification.show("Has cerrado sesión satisfactoriamente.");
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		String username = String.valueOf(getSession().getAttribute("user"));
		userText.setValue("Hola " + username);
	}
}
