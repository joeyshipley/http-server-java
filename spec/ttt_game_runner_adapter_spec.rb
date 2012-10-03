require 'spec_helper'

describe TttGameRunnerAdapter do

  let(:adapter) { TttGameRunnerAdapter.new }
  let!(:input_adapter) { double("input_adapter") }
  let!(:game_engine) { double("game_engine") }

  before(:each) do
    TttGameInputAdapter.stub(:new) { input_adapter }
    GameEngine.stub(:new) { game_engine }
  end

  describe "When starting up the game" do
    it "calls the game engine's start logic" do
      game_engine.should_receive(:start)
      adapter.start_game
    end
  end

  describe "When passing input into the server game runner adapter" do
    it "sends the value to the input adapter" do
      input_adapter.should_receive(:input=).at_least(1)
      adapter.input("rawr")
    end
  end
end
