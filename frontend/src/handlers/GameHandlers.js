import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

export const initializeWebSocket = (
  setBoard,
  setTurn,
  setStatus,
  setPossibleMoves,
  setGameId,
  setCheck,
  setCheckMate
) => {
  const socket = new SockJS("http://localhost:8080/ws");
  const client = new Client({
    webSocketFactory: () => socket,
     debug: (str) => console.log(str),
    onConnect: () => {
      setStatus("CONNECTED");
      client.subscribe("/topic/board", (message) => {
        const data = JSON.parse(message.body);
        console.log(data)
        if (Array.isArray(data.pieces)) {
          setBoard(
            data.pieces.map((row, rowIndex) =>
              row.map((piece, columnIndex) =>
                piece !== null
                  ? { ...piece, chessPosition: piece.chessPosition }
                  : {
                      chessPosition: {
                        column: String.fromCharCode(97 + columnIndex),
                        row: 8 - rowIndex,
                      },
                    }
              )
            )
          );
          setTurn(data.currentPlayer);
          setGameId(data.gameId);
          console.log(" check: " + data.check)
          setCheck(data.check);
           console.log(" check mate: " + data.checkMate)
          setCheckMate(data.checkMate);
        }
      });

      client.subscribe("/topic/possibleMoves", (message) => {
        const data = JSON.parse(message.body);

        setPossibleMoves(data);
      });

      client.publish({ destination: "/app/init" });
    },
    onWebSocketClose: () => setStatus("CONNECTION LOST"),
    onWebSocketError: (error) => setStatus("ERROR ON WEBSOCKET"),
  });
  client.activate();
  return client;
};

export const handleMove = (source, target, stompClient, setStatus) => {
  if (stompClient) {
    const moveData = {
      sourceRow: source.row,
      sourceColumn: source.column,
      targetRow: target ? target.row : null,
      targetColumn: target ? target.column : null,
    };
    stompClient.publish({
      destination: "/app/move",
      body: JSON.stringify(moveData),
    });
  } else {
    setStatus("Not connected at server.");
  }
};

export const handlePieceSelection = (
  piece,
  stompClient,
  setSelectedPiece,
  setPossibleMoves,
  setStatus
) => {
  if (piece && stompClient) {
    const possibleMovesData = {
      sourceRow: piece.chessPosition.row,
      sourceColumn: piece.chessPosition.column,
    };
    stompClient.publish({
      destination: "/app/possibleMoves",
      body: JSON.stringify(possibleMovesData),
    });
    setSelectedPiece(piece);
  } else {
    setStatus("Not connected at server.");
  }
};
