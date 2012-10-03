class TttGameInputAdapter
  LOG_FILE = "./lib/ttt/ttt_output_message.txt"
  attr_reader :log
  attr_reader :input_value
  attr_reader :wait_cycle_count

  def initialize
    clear_log
  end

  def output(value)
    @log += value
    write_log_file
  end

  def input
    write_log_file
    response = wait_for_player_input
    clear_log

    response
  end

  def input=(value)
    @input_value = value
  end

  private

  def wait_for_player_input
    @input_value = nil
    @wait_cycle_count = 0
    while @input_value.nil?
      @wait_cycle_count += 1 # manually wait for input from the game adapter.
    end
    @input_value.to_s
  end

  def write_log_file
    File.open(LOG_FILE, "w") { |file| file.write(@log) }
  end

  def clear_log
    @log = ""
    write_log_file
  end
end
