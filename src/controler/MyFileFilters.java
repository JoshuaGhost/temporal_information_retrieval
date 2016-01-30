package controler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.filefilter.IOFileFilter;

public class MyFileFilters implements FileFilter, IOFileFilter{


	@Override
	public boolean accept(File arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean accept(File pathname) {
		String pattern = "(.*)xml$";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(pathname.getName());
		return m.find();
	}
}
