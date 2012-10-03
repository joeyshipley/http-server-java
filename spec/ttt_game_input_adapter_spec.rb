require 'spec_helper'

describe TttGameInputAdapter do

  let(:adapter) { TttGameInputAdapter.new }

  describe "When receiving output" do
    before(:each) do
      adapter.stub(:write_log_file)
    end

    it "adds the messages to the log" do
      adapter.output "hello log"
      adapter.log.should == "hello log"
    end

    it "writes the log to the file" do
      adapter.should_receive(:write_log_file).at_least(1)
      adapter.output "hello log"
    end

    it "continues to write to the log until it's been cleared" do
      adapter.output "1"
      adapter.output "2"
      adapter.log.should == "12"
    end
  end

  describe "When asking for player input" do
    before(:each) do
      adapter.stub(:write_log_file)
      adapter.stub(:clear_log)
    end

    it "looks for the players input" do
      input_received = ""
      t = Thread.new do
        input_received = adapter.input
      end
      adapter.input = "1"
      adapter.wait_cycle_count.should > 0
    end

    it "clears the log after it receives the input" do
      adapter.should_receive(:clear_log).at_least(1)
      t = Thread.new do
        adapter.input
      end
      adapter.input = "1"
    end

    it "writes the previous log before waiting for input" do
      adapter.should_receive(:write_log_file).at_least(1)
      t = Thread.new do
        adapter.input
      end
      adapter.input = "1"
    end
  end

  describe "when it stores the logs for the servers use" do
    it "puts the log into a file" do
      adapter.output("success")
      adapter.instance_eval { write_log_file }
      contents = File.open("./lib/ttt/ttt_output_message.txt", "r").read
      contents.should == "success"
    end
  end
end
