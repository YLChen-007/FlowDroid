package soot.jimple.infoflow.collections.test.junit.inherited.infoflow;

import org.junit.Test;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.infoflow.AbstractInfoflow;
import soot.jimple.infoflow.IInfoflow;
import soot.jimple.infoflow.Infoflow;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.android.data.parsers.PermissionMethodParser;
import soot.jimple.infoflow.cfg.DefaultBiDiICFGFactory;
import soot.jimple.infoflow.collections.strategies.containers.TestConstantStrategy;
import soot.jimple.infoflow.collections.taintWrappers.PrioritizingMethodSummaryProvider;
import soot.jimple.infoflow.config.PreciseCollectionStrategy;
import soot.jimple.infoflow.methodSummary.data.provider.EagerSummaryProvider;
import soot.jimple.infoflow.methodSummary.data.provider.IMethodSummaryProvider;
import soot.jimple.infoflow.methodSummary.taintWrappers.SummaryTaintWrapper;
import soot.jimple.infoflow.methodSummary.taintWrappers.TaintWrapperFactory;
import soot.jimple.infoflow.results.InfoflowResults;
import soot.jimple.infoflow.results.ResultSinkInfo;
import soot.jimple.infoflow.results.ResultSourceInfo;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinitionProvider;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.options.Options;
import soot.util.dot.DotGraph;
import soot.util.queue.QueueReader;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyTests extends soot.jimple.infoflow.test.junit.ArrayTests {
	@Override
	protected AbstractInfoflow createInfoflowInstance() {
		AbstractInfoflow result = new Infoflow("", false, new DefaultBiDiICFGFactory());
		result.getConfig().getSolverConfiguration().setDataFlowSolver(InfoflowConfiguration.DataFlowSolver.SparseContextFlowSensitive);
		result.getConfig().setCallgraphAlgorithm(InfoflowConfiguration.CallgraphAlgorithm.SPARK);
		result.getConfig().setPathAgnosticResults(false);
		result.getConfig().getPathConfiguration().setPathReconstructionMode(InfoflowConfiguration.PathReconstructionMode.Fast);
		result.getConfig().setEnableTypeChecking(false);

		try {
			ArrayList<IMethodSummaryProvider> providers = new ArrayList<>();
			providers.add(new EagerSummaryProvider(TaintWrapperFactory.DEFAULT_SUMMARY_DIR));
			PrioritizingMethodSummaryProvider sp = new PrioritizingMethodSummaryProvider(providers);
			result.setTaintWrapper(new SummaryTaintWrapper(sp).setContainerStrategyFactory(TestConstantStrategy::new));
		} catch (Exception e) {
			throw new RuntimeException();
		}

		return result;
	}

	@Test(timeout = 300000000)
	public void testMapreduce() {
		IInfoflow infoflow = initInfoflow();
		List<String> epoints = new ArrayList<String>();
//		epoints.add("<soot.jimple.infoflow.test.ArrayTestCode: void concreteWriteReadDiffPosTest()>");
		// modify #main
		epoints.add("<org.apache.hadoop.yarn.server.resourcemanager.ResourceManager: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.yarn.server.nodemanager.NodeManager: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.yarn.applications.distributedshell.Client: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.yarn.server.nodemanager.NodeManager: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.yarn.applications.distributedshell.ApplicationMaster: void main(java.lang.String[])>");
//		appPath = "D:\\project\\vscode\\test1\\build\\classes\\java\\main";


		appPath = "";
		String appDir = "D:\\instrumentJar\\hadoop\\V3_4_0\\yarn";
		// find all files in the directory
		File file = new File(appDir);
		File[] files = file.listFiles();
		for (File f : files) {
			if (f.isFile()) {
				appPath += (f.getAbsolutePath() + File.pathSeparator);
			}
		}

//		appPath = "D:\\project\\vscode\\test1\\build\\libs\\Test1-1.0-SNAPSHOT.jar";
//		appPath = "D:\\project\\vscode\\test1\\build\\libs";
//		appPath = "D:\\project\\vscode\\test1\\build\\classes";
		sources.clear();
		sources.add("<org.apache.hadoop.mapred.JvmTask: org.apache.hadoop.mapred.Task getTask()>");
		sinks.clear();
//		org.example.Main#sink
		sinks.add("<org.apache.hadoop.mapred.ReduceTask: void run(org.apache.hadoop.mapred.JobConf,org.apache.hadoop.mapred.TaskUmbilicalProtocol)>");
//		sinks.add("<org.example.Main: void others(java.lang.String)>");

		// 确保正确配置 Soot 选项
		Options.v().set_src_prec(Options.src_prec_apk);
		Options.v().set_process_dir(Collections.singletonList(appPath));
		Options.v().set_android_jars(libPath);
		Options.v().set_output_dir("MyResult");

		infoflow.computeInfoflow(appPath, libPath, epoints, sources, sinks);


//		export callgraph
		CallGraph cg = Scene.v().getCallGraph();

//		System.out.println(cg);
// 创建一个DOTGraph对象
	/*	DOTGraph dotGraph = new DOTGraph("callgraph");

// 遍历调用图中的所有边
		for (Edge edge : cg) {
			// 添加节点和边到DOT图中
			dotGraph.drawNode(edge.src().toString());
			dotGraph.drawNode(edge.tgt().toString());
			dotGraph.drawEdge(edge.src().toString(), edge.tgt().toString());
		}

// 导出DOT图到文件
		dotGraph.plot("callgraph.dot");*/

		InfoflowResults results = infoflow.getResults();
		// 处理结果
	}


	@Test(timeout = 300000000)
	public void testHbaseInstrument() {
		IInfoflow infoflow = initInfoflow();
		List<String> epoints = new ArrayList<String>();
//		epoints.add("<soot.jimple.infoflow.test.ArrayTestCode: void concreteWriteReadDiffPosTest()>");
		// modify #main
		epoints.add("<org.apache.hadoop.hbase.master.HMaster: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.hbase.regionserver.HRegionServer: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.hbase.zookeeper.HQuorumPeer: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.hbase.master.HBackupMaster: void main(java.lang.String[])>");
//		appPath = "D:\\project\\vscode\\test1\\build\\classes\\java\\main";

		appPath = "";
		String appDir = "D:\\instrumentJar\\hbase";
		// find all files in the directory
		File file = new File(appDir);
		File[] files = file.listFiles();
		for (File f : files) {
			if (f.isFile()) {
				appPath += (f.getAbsolutePath() + File.pathSeparator);
			}
		}

//		appPath = "D:\\project\\vscode\\test1\\build\\libs\\Test1-1.0-SNAPSHOT.jar";
//		appPath = "D:\\project\\vscode\\test1\\build\\libs";
//		appPath = "D:\\project\\vscode\\test1\\build\\classes";
		sources.clear();
		sources.add("<org.apache.hadoop.mapred.JvmTask: org.apache.hadoop.mapred.Task getTask()>");
		sinks.clear();
//		org.example.Main#sink
		sinks.add("<org.apache.hadoop.mapred.ReduceTask: void run(org.apache.hadoop.mapred.JobConf,org.apache.hadoop.mapred.TaskUmbilicalProtocol)>");
//		sinks.add("<org.example.Main: void others(java.lang.String)>");

		// 确保正确配置 Soot 选项
		Options.v().set_src_prec(Options.src_prec_apk);
		Options.v().set_process_dir(Collections.singletonList(appPath));
		Options.v().set_android_jars(libPath);
		Options.v().set_output_dir("MyResult");

		infoflow.computeInfoflow(appPath, libPath, epoints, sources, sinks);


//		export callgraph
		CallGraph cg = Scene.v().getCallGraph();

		exportCallGraphToDot(cg,"hbase-cg.dot");
//		System.out.println(cg);
// 创建一个DOTGraph对象
		// 处理结果
	}

	@Test(timeout = 300000000)
	public void testHbaseNormal() {
		IInfoflow infoflow = initInfoflow();
		List<String> epoints = new ArrayList<String>();
//		epoints.add("<soot.jimple.infoflow.test.ArrayTestCode: void concreteWriteReadDiffPosTest()>");
		// modify #main
		epoints.add("<org.apache.hadoop.hbase.master.HMaster: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.hbase.regionserver.HRegionServer: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.hbase.zookeeper.HQuorumPeer: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.hbase.master.HBackupMaster: void main(java.lang.String[])>");
//		appPath = "D:\\project\\vscode\\test1\\build\\classes\\java\\main";

		appPath = "";
		String appDir = "E:\\Jar\\hbase-2.6.0-bin\\hbase-2.6.0\\analysis";
		// find all files in the directory
		File file = new File(appDir);
		File[] files = file.listFiles();
		for (File f : files) {
			if (f.isFile()) {
				appPath += (f.getAbsolutePath() + File.pathSeparator);
			}
		}

//		appPath = "D:\\project\\vscode\\test1\\build\\libs\\Test1-1.0-SNAPSHOT.jar";
//		appPath = "D:\\project\\vscode\\test1\\build\\libs";
//		appPath = "D:\\project\\vscode\\test1\\build\\classes";
		sources.clear();
		sources.add("<org.apache.hadoop.mapred.JvmTask: org.apache.hadoop.mapred.Task getTask()>");
		sinks.clear();
//		org.example.Main#sink
		sinks.add("<org.apache.hadoop.mapred.ReduceTask: void run(org.apache.hadoop.mapred.JobConf,org.apache.hadoop.mapred.TaskUmbilicalProtocol)>");
//		sinks.add("<org.example.Main: void others(java.lang.String)>");

		// 确保正确配置 Soot 选项
		Options.v().set_src_prec(Options.src_prec_apk);
		Options.v().set_process_dir(Collections.singletonList(appPath));
		Options.v().set_android_jars(libPath);
		Options.v().set_output_dir("MyResult");

		infoflow.computeInfoflow(appPath, libPath, epoints, sources, sinks);


//		export callgraph
		CallGraph cg = Scene.v().getCallGraph();

//		exportCallGraphToDot(cg,"hbase-cg-normal.dot");
//		System.out.println(cg);
// 创建一个DOTGraph对象
		// 处理结果
	}

	private static void exportCallGraphToDot(CallGraph callGraph, String fileName) {
		DotGraph dotGraph = new DotGraph("CallGraph");

		QueueReader<Edge> edges = callGraph.listener();
		while (edges.hasNext()) {
			Edge edge = edges.next();
			SootMethod src = edge.src();
			SootMethod tgt = edge.tgt();

			dotGraph.drawNode(src.getSignature());
			dotGraph.drawNode(tgt.getSignature());
			dotGraph.drawEdge(src.getSignature(), tgt.getSignature());
		}

		try {
			dotGraph.plot(fileName);
			System.out.println("Call graph exported to " + fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public List<String> allJar(String appDir) {
		List<String> jarPaths = new ArrayList<>();
		File file = new File(appDir);
		File[] files = file.listFiles();
		for (File f : files) {
			if (f.isFile()) {
				appPath += (f.getAbsolutePath() + File.pathSeparator);
				jarPaths.add(f.getAbsolutePath());
			}
		}
		return jarPaths;
	}


	@Test(timeout = 300000000)
	public void testDacapo() throws IOException {

		long maxMemory = Runtime.getRuntime().maxMemory() / (1024 * 1024);
		System.out.println("Max memory: " + maxMemory + " MB");


		IInfoflow infoflow = initInfoflow();
		List<String> epoints = new ArrayList<String>();
//		epoints.add("<soot.jimple.infoflow.test.ArrayTestCode: void concreteWriteReadDiffPosTest()>");
		// modify #main
		appPath = "";


		List<String> jarPaths = allJar("/root/taintBySS/dacapo/dacapo-23.11-chopin/jar/batik");

		List<String> allMain = findAllMain(jarPaths);
		epoints.addAll(allMain);


		ISourceSinkDefinitionProvider parser  = PermissionMethodParser.fromFile("../soot-infoflow-android/SourcesAndSinks.txt");

		sources.clear();
		System.out.println(parser.getSources());
		for (ISourceSinkDefinition source : parser.getSources()) {
			sources.add(source.toString());
		}
		sinks.clear();
		for (ISourceSinkDefinition sink : parser.getSinks()) {
			sinks.add(sink.toString());
		}

		infoflow.computeInfoflow(appPath, libPath, epoints, sources, sinks);
		InfoflowResults results = infoflow.getResults();

	}

	public List<String> findAllMain(List<String> jarPaths){


		// Initialize Soot
		Options.v().set_prepend_classpath(true);
		Options.v().set_whole_program(true);
		Options.v().set_process_dir(jarPaths);
		Options.v().set_app(true);
		Options.v().set_allow_phantom_refs(true);
		// Load classes
		Scene.v().loadNecessaryClasses();

		List<String> mainEntryPoint = new ArrayList<>();

		// Iterate through all classes in the Scene
		for (SootClass sootClass : Scene.v().getApplicationClasses()) {
			for (SootMethod method : sootClass.getMethods()) {
				// Check if the method is a 'main' method
				if (method.getName().equals("main") && method.isStatic() && method.isPublic() &&
						method.getParameterCount() == 1 &&
						method.getParameterType(0).toString().equals("java.lang.String[]")) {
					mainEntryPoint.add("<" + sootClass.getName() + ": void main(java.lang.String[])>");
					System.out.println("Found main method: " + method);
				}
			}
		}
		return mainEntryPoint;
	}

	@Test(timeout = 300000000)
	public void testUnit() {
		IInfoflow infoflow = initInfoflow();
		List<String> epoints = new ArrayList<String>();
//		epoints.add("<soot.jimple.infoflow.test.ArrayTestCode: void concreteWriteReadDiffPosTest()>");
		// modify #main
		epoints.add("<org.example.Main: void main(java.lang.String[])>");
		epoints.add("<org.example.javassist.TestAnimal: void main(java.lang.String[])>");
		appPath = "D:\\project\\vscode\\test1\\build\\classes\\java\\main";
//		appPath = "D:\\project\\vscode\\test1\\build\\libs";
//		appPath = "D:\\project\\vscode\\test1\\build\\classes";
		sources.clear();
//		org.example.Main#source()
		sources.add("<org.example.Main: java.lang.String source(java.lang.String)>");
		sinks.clear();
//		org.example.Main#sink
		sinks.add("<org.example.Main: void sink(java.lang.String)>");
//		sinks.add("<org.example.Main: void others(java.lang.String)>");


		/*
		ISourceSinkDefinitionProvider parser = null;
		try {
			if (fileExtension.equals(".xml")) {
				parser = XMLSourceSinkParser.fromFile(sourceSinkFile,
						new ConfigurationBasedCategoryFilter(config.getSourceSinkConfig()));
			} else if (fileExtension.equals(".txt"))
				parser = PermissionMethodParser.fromFile(sourceSinkFile);
			else if (fileExtension.equals(".rifl"))
				parser = new RIFLSourceSinkDefinitionProvider(sourceSinkFile);
			else
				throw new UnsupportedSourceSinkFormatException("The Inputfile isn't a .txt or .xml file.");
		} catch (SAXException ex) {
			throw new IOException("Could not read XML file", ex);
		}

		* */


		// 确保正确配置 Soot 选项
		Options.v().set_src_prec(Options.src_prec_apk);
		Options.v().set_process_dir(Collections.singletonList(appPath));
		Options.v().set_android_jars(libPath);
		Options.v().set_output_dir("MyResult");

		infoflow.computeInfoflow(appPath, libPath, epoints, sources, sinks);
		InfoflowResults results = infoflow.getResults();
		// 处理结果

		/*if (results != null && results.size() > 0) {
			System.out.println("发现数据流路径：");
			for (ResultSinkInfo sink : results.getResults().keySet()) {
				System.out.println("Sink: " + sink);
				for (ResultSourceInfo source : results.getResults().get(sink)) {
					System.out.println("  Source: " + source);
					System.out.println("  Path:");
					for (soot.Unit unit : source.getPath()) {
						System.out.println("    " + unit);
					}
				}
			}
		} else {
			System.out.println("未发现数据流路径。");
		}*/


		// We are more precise
//		negativeCheckInfoflow(infoflow);
	}


	@Test(timeout = 3600*24*20*1000)
	public void testYarnSIF() throws IOException {

		long maxMemory = Runtime.getRuntime().maxMemory();
        System.out.println("Max Memory: " + (maxMemory / (1024 * 1024)) + " MB");

		IInfoflow infoflow = initInfoflow();
		List<String> epoints = new ArrayList<String>();
		// modify #main
		epoints.add("<org.apache.hadoop.yarn.server.resourcemanager.ResourceManager: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.yarn.server.nodemanager.NodeManager: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.yarn.applications.distributedshell.Client: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.yarn.server.nodemanager.NodeManager: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.yarn.applications.distributedshell.ApplicationMaster: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.mapred.YarnChild: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.yarn.webapp.view.DefaultPage: void render()>");
		epoints.add("<org.apache.hadoop.yarn.webapp.view.TextView: void puts(java.lang.Object[])>");

		/* epoints.add("<org.apache.hadoop.hdfs.server.namenode.NameNode: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.hdfs.server.datanode.DataNode: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.yarn.server.resourcemanager.ResourceManager: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.yarn.server.nodemanager.NodeManager: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.fs.FsShell: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.yarn.client.cli.ApplicationCLI: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.hdfs.tools.DFSAdmin: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.util.VersionInfo: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.yarn.server.webproxy.WebAppProxyServer: void main(java.lang.String[])>");
		epoints.add("<org.apache.hadoop.security.UserGroupInformation: void main(java.lang.String[])>"); */
			

		ISourceSinkDefinitionProvider parser  = PermissionMethodParser.fromFile("../soot-infoflow-android/yarnSourceAndSinks.txt");

		sources.clear();
		System.out.println(parser.getSources());
		for (ISourceSinkDefinition source : parser.getSources()) {
			sources.add(source.toString());
		}
		sinks.clear();
		for (ISourceSinkDefinition sink : parser.getSinks()) {
			sinks.add(sink.toString());
		}

		appPath = "";
		List<String> jarPaths = allJar("../analysisJar/yarn");
		
		// List<String> allMain = findAllMain(jarPaths);

		// allMain.remove("<org.apache.hadoop.registry.cli.RegistryCli: void main(java.lang.String[])>");
		// allMain.remove("<org.apache.hadoop.registry.server.dns.RegistryDNSServer: void main(java.lang.String[])>");
		// allMain.remove("<org.apache.hadoop.yarn.service.ServiceMaster: void main(java.lang.String[])>");

// wrong main
// test main: <org.apache.hadoop.registry.cli.RegistryCli: void main(java.lang.String[])> 59
// test main: <org.apache.hadoop.registry.server.dns.RegistryDNSServer: void main(java.lang.String[])> 60

/* 		int i = 0;
		for (String main : allMain) {
			i ++;
			System.out.println("test main: " + main  + " " + i);
			if (i > 74) {
				infoflow.computeInfoflow(appPath, libPath, main, sources, sinks);
			}
		} */

		// epoints.addAll(allMain);

		infoflow.computeInfoflow(appPath, libPath, epoints, sources, sinks);

		InfoflowResults results = infoflow.getResults();
		results.printResults(); 
		// 处理结果
	}
}
