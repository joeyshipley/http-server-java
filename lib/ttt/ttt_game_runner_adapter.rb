$LOAD_PATH << './lib/ttt'
$LOAD_PATH << './lib/ttt/gems'
$LOAD_PATH << './lib/ttt/ttt_game/lib'

require 'ttt_game_input_adapter'
require 'game_engine'
require 'r18n-desktop'
include R18n::Helpers

R18n.default_places = './lib/ttt/ttt_game/config/locales/'
R18n.set 'en'

class TttGameRunnerAdapter
  def initialize
    @input_adapter = TttGameInputAdapter.new
    @game_engine = nil
  end

  def start_game
    t = Thread.new do
      @game_engine = GameEngine.new(@input_adapter)
      @game_engine.start
    end
  end

  def input(value)
    @input_adapter.input = value
  end
end

