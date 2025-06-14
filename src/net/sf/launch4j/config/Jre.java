/*
	Launch4j (http://launch4j.sourceforge.net/)
	Cross-platform Java application wrapper for creating Windows native executables.

	Copyright (c) 2004, 2015 Grzegorz Kowal
	All rights reserved.

	Redistribution and use in source and binary forms, with or without modification,
	are permitted provided that the following conditions are met:
	
	1. Redistributions of source code must retain the above copyright notice,
	   this list of conditions and the following disclaimer.
	
	2. Redistributions in binary form must reproduce the above copyright notice,
	   this list of conditions and the following disclaimer in the documentation
	   and/or other materials provided with the distribution.
	
	3. Neither the name of the copyright holder nor the names of its contributors
	   may be used to endorse or promote products derived from this software without
	   specific prior written permission.
	
	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
	AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
	THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
	ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
	FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
	(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
	LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
	AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
	OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
	OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

/*
 * Created on Apr 21, 2005
 */
package net.sf.launch4j.config;

import java.util.List;

import net.sf.launch4j.binding.IValidatable;
import net.sf.launch4j.binding.Validator;

/**
 * @author Copyright (C) 2005 Grzegorz Kowal
 */
public class Jre implements IValidatable {

	public static final String VERSION_PATTERN = "(1\\.\\d\\.\\d(_\\d{1,3})?)|[1-9][0-9]{0,2}(\\.\\d{1,3}){0,2}";
	public static final String DEFAULT_PATH = "%JAVA_HOME%;%PATH%";

	private String path;
	private boolean requiresJdk;
	private boolean requires64Bit;
	private boolean requiresJfx;
	private String minVersion;
	private String maxVersion;
	private Integer initialHeapSize;
	private Integer initialHeapPercent;
	private Integer maxHeapSize;
	private Integer maxHeapPercent;
	private List<String> options;

	public void checkInvariants() {
		Validator.checkOptString(minVersion, 20, VERSION_PATTERN, "jre.minVersion",
				Messages.getString("Jre.min.version"));
		Validator.checkOptString(maxVersion, 20, VERSION_PATTERN, "jre.maxVersion",
				Messages.getString("Jre.max.version"));
		Validator.checkString(path, Validator.MAX_PATH, "jre.path", Messages.getString("Jre.path"));
		if (!Validator.isEmpty(maxVersion)) {
			Validator.checkFalse(Validator.isEmpty(minVersion), "jre.minVersion",
					Messages.getString("Jre.specify.min.version"));
			Validator.checkTrue(JreVersion.parseString(minVersion).compareTo(JreVersion.parseString(maxVersion)) < 0,
					"jre.maxVersion", Messages.getString("Jre.max.greater.than.min"));
		}
		Validator.checkTrue(initialHeapSize == null || maxHeapSize != null, "jre.maxHeapSize",
				Messages.getString("Jre.initial.and.max.heap"));
		Validator.checkTrue(initialHeapSize == null || initialHeapSize.intValue() > 0, "jre.initialHeapSize",
				Messages.getString("Jre.initial.heap"));
		Validator.checkTrue(
				maxHeapSize == null
						|| (maxHeapSize.intValue() >= ((initialHeapSize != null) ? initialHeapSize.intValue() : 1)),
				"jre.maxHeapSize", Messages.getString("Jre.max.heap"));
		Validator.checkTrue(initialHeapPercent == null || maxHeapPercent != null, "jre.maxHeapPercent",
				Messages.getString("Jre.initial.and.max.heap"));
		if (initialHeapPercent != null) {
			Validator.checkRange(initialHeapPercent.intValue(), 1, 100, "jre.initialHeapPercent",
					Messages.getString("Jre.initial.heap.percent"));
		}
		if (maxHeapPercent != null) {
			Validator.checkRange(maxHeapPercent.intValue(),
					initialHeapPercent != null ? initialHeapPercent.intValue() : 1, 100, "jre.maxHeapPercent",
					Messages.getString("Jre.max.heap.percent"));
		}
		Validator.checkOptStrings(options, Validator.MAX_ARGS, Validator.MAX_ARGS, "[^\"]*|([^\"]*\"[^\"]*\"[^\"]*)*",
				"jre.options", Messages.getString("Jre.jvm.options"),
				Messages.getString("Jre.jvm.options.unclosed.quotation"));

		// Quoted variable references: "[^%]*|([^%]*\"([^%]*%[^%]+%[^%]*)+\"[^%]*)*"
		Validator.checkOptStrings(options, Validator.MAX_ARGS, Validator.MAX_ARGS,
				"[^%]*|([^%]*([^%]*%[^%]*%[^%]*)+[^%]*)*", "jre.options", Messages.getString("Jre.jvm.options"),
				Messages.getString("Jre.jvm.options.variable"));
	}

	/** JVM options */
	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	/** Max Java version (x.x.x) */
	public String getMaxVersion() {
		return maxVersion;
	}

	public void setMaxVersion(String maxVersion) {
		this.maxVersion = maxVersion;
	}

	/** Min Java version (x.x.x) */
	public String getMinVersion() {
		return minVersion;
	}

	public void setMinVersion(String minVersion) {
		this.minVersion = minVersion;
	}

	public boolean getRequiresJdk() {
		return this.requiresJdk;
	}

	public void setRequiresJdk(boolean requiresJdk) {
		this.requiresJdk = requiresJdk;
	}

	/** JRE path */
	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean getRequires64Bit() {
		return requires64Bit;
	}

	public void setRequires64Bit(boolean requires64Bit) {
		this.requires64Bit= requires64Bit;
	}

	public boolean getRequiresJfx() {
		return requiresJfx;
	}

	public void setRequiresJfx(boolean requiresJfx) {
		this.requiresJfx = requiresJfx;
	}

	/** Initial heap size in MB */
	public Integer getInitialHeapSize() {
		return initialHeapSize;
	}

	public void setInitialHeapSize(Integer initialHeapSize) {
		this.initialHeapSize = getInteger(initialHeapSize);
	}

	/** Max heap size in MB */
	public Integer getMaxHeapSize() {
		return maxHeapSize;
	}

	public void setMaxHeapSize(Integer maxHeapSize) {
		this.maxHeapSize = getInteger(maxHeapSize);
	}
	
	public Integer getInitialHeapPercent() {
    	return initialHeapPercent;
    }

	public void setInitialHeapPercent(Integer initialHeapPercent) {
    	this.initialHeapPercent = getInteger(initialHeapPercent);
    }

	public Integer getMaxHeapPercent() {
    	return maxHeapPercent;
    }

	public void setMaxHeapPercent(Integer maxHeapPercent) {
    	this.maxHeapPercent = getInteger(maxHeapPercent);
    }

	/** Convert 0 to null */
	private Integer getInteger(Integer i) {
		return i != null && i.intValue() == 0 ? null : i;
	}
}
